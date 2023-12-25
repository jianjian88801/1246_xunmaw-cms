package com.xunmaw.cms.common.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Bean拷贝工具类
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Slf4j
@UtilityClass
public class CopyUtil {

    /**
     * @param source 源
     * @param dest   目标
     */
    public static void copy(Object source, Object dest) {
        try {
            if (source != null || dest != null) {
                // 获取属性
                BeanInfo sourceBean = Introspector.getBeanInfo(source.getClass(), Object.class);
                PropertyDescriptor[] sourceProperty = sourceBean.getPropertyDescriptors();
                BeanInfo destBean = Introspector.getBeanInfo(dest.getClass(), Object.class);
                PropertyDescriptor[] destProperty = destBean.getPropertyDescriptors();
                for (int i = 0; i < sourceProperty.length; i++) {
                    for (int j = 0; j < destProperty.length; j++) {
                        if (sourceProperty[i].getName().equals(destProperty[j].getName())) {
                            try {
                                // 调用source的getter方法和dest的setter方法
                                destProperty[j].getWriteMethod().invoke(dest, sourceProperty[i].getReadMethod().invoke(source));
                                break;
                            } catch (Exception e) {
                                log.info("属性赋值失败," + sourceProperty[i].getName() + e.getMessage());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.info("对象拷贝失败, source: {}, dest: {}", source, dest, e);
        }
    }

    /**
     * @param source 源
     * @param dest   目标
     */
    public static void copyNotNull(Object source, Object dest) {
        try {
            if (source == null || dest == null) {
                return;
            }
            // 获取属性
            BeanInfo sourceBean = Introspector.getBeanInfo(source.getClass(), Object.class);
            PropertyDescriptor[] sourceProperty = sourceBean.getPropertyDescriptors();
            BeanInfo destBean = Introspector.getBeanInfo(dest.getClass(), Object.class);
            PropertyDescriptor[] destProperty = destBean.getPropertyDescriptors();
            for (int i = 0; i < sourceProperty.length; i++) {
                for (int j = 0; j < destProperty.length; j++) {
                    if (sourceProperty[i].getName().equals(destProperty[j].getName()) && Objects.nonNull(sourceProperty[i].getReadMethod().invoke(source))) {
                        try {
                            // 调用source的getter方法和dest的setter方法
                            destProperty[j].getWriteMethod().invoke(dest, sourceProperty[i].getReadMethod().invoke(source));
                            break;
                        } catch (Exception e) {
                            log.info("属性赋值失败," + sourceProperty[i].getName() + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.info("对象拷贝失败, source: {}, dest: {}", source, dest, e);
        }
    }

    /***
     *
     * @param source
     *            源
     * @param clazz
     *            目标类
     * @return 目标类实例
     */
    public static <T> T getCopy(Object source, Class<T> clazz) {
        T dest = null;
        try {
            dest = clazz.newInstance();
            copy(source, dest);
        } catch (Exception e) {
            log.info("对象复制错误:" + e.getMessage(), e);
        }
        return dest;
    }

    /***
     *
     * @param source
     *            源
     * @param dest
     *            目标类
     * @param ignoreProperties
     *            过滤掉的属性
     */
    public static Object getCopy(Object source, Object dest, String... ignoreProperties) {
        try {
            BeanUtils.copyProperties(source, dest, ignoreProperties);
        } catch (Exception e) {
            log.info("对象复制错误:" + e.getMessage(), e);
        }
        return dest;
    }

    @SuppressWarnings("rawtypes")
    public static <T> List<T> getCopyList(List sources, Class<T> clazz) {
        List<T> clazzs = new ArrayList<>();
        if (sources == null) {
            return clazzs;
        }
        for (Object source : sources) {
            try {
                T dest = clazz.newInstance();
                copy(source, dest);
                clazzs.add(dest);
            } catch (InstantiationException | IllegalAccessException e) {
                log.info("对象复制错误:" + e.getMessage(), e);
            }
        }
        return clazzs;
    }
}