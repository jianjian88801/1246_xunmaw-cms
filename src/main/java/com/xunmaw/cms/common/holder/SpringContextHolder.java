package com.xunmaw.cms.common.holder;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring容器持有类，可以获取spring管理的bean
 *
 * @author xunmaw
 * @version V1.0
 * @date 2019年9月11日
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        appContext = applicationContext;
    }

    /**
     * 通过name获取Bean.
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return appContext.getBean(name);

    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return appContext.getBean(clazz);
    }

    /**
     * 通过name,以及clazz返回指定的Bean.
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return appContext.getBean(name, clazz);
    }

}
