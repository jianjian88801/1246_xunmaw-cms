package com.xunmaw.cms.common.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import cn.hutool.core.util.StrUtil;
import com.xunmaw.cms.common.shiro.MyShiroRealm;
import com.xunmaw.cms.common.shiro.filter.KickoutSessionControlFilter;
import com.xunmaw.cms.common.util.CoreConst;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.servlet.Filter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro相关配置
 *
 * @author xunmaw
 * @version V1.0
 * @date 2019年9月11日
 */
@Configuration
public class ShiroConfig {

    @Value("${spring.cache.type:none}")
    private CacheType cacheType;

    @Lazy
    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * ShiroDialect，为了在thymeleaf里使用shiro的标签的bean
     *
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，因为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     * <p>
     * Filter Chain定义说明
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 登录url
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/admin");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/error/403");
        //自定义拦截器
        Map<String, Filter> filtersMap = new LinkedHashMap<>();
        //限制同一帐号同时在线的个数。
        filtersMap.put("kickout", kickoutSessionControlFilter(securityManager));
        shiroFilterFactoryBean.setFilters(filtersMap);
        // 设置全局filter
        shiroFilterFactoryBean.setGlobalFilters(Collections.emptyList());
        return shiroFilterFactoryBean;
    }

    /**
     * cookie对象;
     *
     * @return
     */
    public SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    /**
     * cookie管理对象;记住我功能
     *
     * @return
     */
    public RememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    @Bean(name = "securityManager")
    public SecurityManager securityManager(MyShiroRealm shiroRealm,
										   @Lazy CacheManager cacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm.
        securityManager.setRealm(shiroRealm);
        /*记住我*/
        securityManager.setRememberMeManager(rememberMeManager());
        // 自定义缓存实现 使用redis或memory
        securityManager.setCacheManager(cacheManager);
        // 自定义session管理 使用redis或memory
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 禁用url自动拼接的 ;jsessionid= ，因为shiro 1.6.0 新增了org.apache.shiro.web.filter.InvalidRequestFilter 会拦截url中的分号、反斜杠以及非ASCII码
        // 详情可参考 https://mp.weixin.qq.com/s?__biz=MzIxNjkwODg4OQ==&mid=2247484465&idx=1&sn=04db0c26159dee0a8443c74c7ab58ebe&chksm=97809107a0f718114c20a3ad881e31810272593007d515f03a372230a64adfff53ebdc55c141&scene=126&
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setSessionDAO(shiroSessionDAO());
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public IRedisManager shiroRedisManager() {
        if (redisProperties.getCluster() != null) {
            RedisClusterManager redisClusterManager = new RedisClusterManager();
            redisClusterManager.setHost(StrUtil.join(StrUtil.COMMA, redisProperties.getCluster().getNodes()));
            if (StrUtil.isNotBlank(redisProperties.getPassword())) {
                redisClusterManager.setPassword(redisProperties.getPassword());
            }
            return redisClusterManager;
        } else if (redisProperties.getSentinel() != null) {
            RedisSentinelManager redisSentinelManager = new RedisSentinelManager();
            redisSentinelManager.setHost(StrUtil.join(StrUtil.COMMA, redisProperties.getSentinel().getNodes()));
            if (StrUtil.isNotBlank(redisProperties.getSentinel().getPassword())) {
                redisSentinelManager.setPassword(redisProperties.getSentinel().getPassword());
            }
            redisSentinelManager.setDatabase(redisProperties.getDatabase());
            redisSentinelManager.setMasterName(redisProperties.getSentinel().getMaster());
            return redisSentinelManager;
        } else {
            RedisManager redisManager = new RedisManager();
            redisManager.setHost(StrUtil.join(StrUtil.COLON, redisProperties.getHost(), redisProperties.getPort()));
            if (StrUtil.isNotBlank(redisProperties.getPassword())) {
                redisManager.setPassword(redisProperties.getPassword());
            }
            redisManager.setDatabase(redisProperties.getDatabase());
            return redisManager;
        }
    }

    /**
     * 使用redis或memory实现缓存管理器
     *
     * @return
     */
    @Bean
    public CacheManager shiroCacheManager() {
        if (cacheType == CacheType.REDIS) {
            RedisCacheManager redisCacheManager = new RedisCacheManager();
            redisCacheManager.setRedisManager(shiroRedisManager());
            redisCacheManager.setPrincipalIdFieldName("userId");
            return redisCacheManager;
        }
        return new MemoryConstrainedCacheManager();
    }


    /**
     * 使用redis或memory实现 shiro sessionDao层的实现
     */
    @Bean
    public SessionDAO shiroSessionDAO() {
        if (cacheType == CacheType.REDIS) {
            RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
            redisSessionDAO.setKeyPrefix(CoreConst.SHIRO_REDIS_SESSION_PREFIX);
            redisSessionDAO.setRedisManager(shiroRedisManager());
            return redisSessionDAO;
        }
        return new MemorySessionDAO();
    }

    /**
     * 限制同一账号登录同时登录人数控制
     *
     * @return
     */
    public KickoutSessionControlFilter kickoutSessionControlFilter(SessionManager sessionManager) {
        KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        //这里我们还是用之前shiro使用的redisManager()实现的cacheManager()缓存管理
        //也可以重新另写一个，重新配置缓存时间之类的自定义缓存属性
        kickoutSessionControlFilter.setCacheManager(shiroCacheManager());
        //用于根据会话ID，获取会话进行踢出操作的；
        kickoutSessionControlFilter.setSessionManager(sessionManager);
        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序。
        kickoutSessionControlFilter.setKickoutAfter(false);
        //同一个用户最大的会话数，默认5；比如5的意思是同一个用户允许最多同时五个人登录；
        kickoutSessionControlFilter.setMaxSession(5);
        //被踢出后重定向到的地址；
        kickoutSessionControlFilter.setKickoutUrl("/kickout");
        return kickoutSessionControlFilter;
    }


}
