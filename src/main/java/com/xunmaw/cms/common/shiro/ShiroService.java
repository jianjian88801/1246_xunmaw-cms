package com.xunmaw.cms.common.shiro;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.xunmaw.cms.common.config.properties.FileUploadProperties;
import com.xunmaw.cms.common.config.properties.StaticizeProperties;
import com.xunmaw.cms.common.util.CoreConst;
import com.xunmaw.cms.module.admin.model.Permission;
import com.xunmaw.cms.module.admin.service.PermissionService;
import lombok.AllArgsConstructor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * 初始化、动态更新shiro用户权限
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Service
@AllArgsConstructor
public class ShiroService {

    private final PermissionService permissionService;

    private final ShiroFilterFactoryBean shiroFilterFactoryBean;

    private final FileUploadProperties fileUploadProperties;
    private final StaticizeProperties staticizeProperties;

    @PostConstruct
    public void init() {
        updatePermission();
    }

    /**
     * 初始化权限
     */
    public Map<String, String> loadFilterChainDefinitions() {
        // 权限控制map.从数据库获取
        Map<String, String> filterChainDefinitionMap = MapUtil.newHashMap(true);
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/blog/**", "anon");
        filterChainDefinitionMap.put("/register", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/kickout", "anon");
        filterChainDefinitionMap.put("/error/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/libs/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/verificationCode", "anon");
        filterChainDefinitionMap.put(fileUploadProperties.getAccessPathPattern(), "anon");
        filterChainDefinitionMap.put(staticizeProperties.getAccessPathPattern(), "anon");
        List<Permission> permissionList = permissionService.selectAll(CoreConst.STATUS_VALID);
        for (Permission permission : permissionList) {
            if (StrUtil.isAllNotBlank(permission.getUrl(), permission.getPerms())) {
                String perm = "perms[" + permission.getPerms() + ']';
                filterChainDefinitionMap.put(permission.getUrl(), perm + ",kickout");
            }
        }
        filterChainDefinitionMap.put("/**", "user,kickout");
        return filterChainDefinitionMap;
    }

    /**
     * 重新加载权限
     */
    public void updatePermission() {
        synchronized (shiroFilterFactoryBean) {

            AbstractShiroFilter shiroFilter;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
            }

            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
                    .getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver
                    .getFilterChainManager();

            // 清空老的权限控制
            manager.getFilterChains().clear();

            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            shiroFilterFactoryBean.setFilterChainDefinitionMap(loadFilterChainDefinitions());
            // 重新构建生成
            Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
            chains.forEach((url, perm) -> manager.createChain(url, StrUtil.cleanBlank(perm)));
        }
    }
}
