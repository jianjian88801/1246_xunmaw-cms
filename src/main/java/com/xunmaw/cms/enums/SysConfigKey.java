package com.xunmaw.cms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 接口响应状态枚举类
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Getter
@AllArgsConstructor
public enum SysConfigKey {
    /**
     * 云存储配置
     */
    CLOUD_STORAGE_CONFIG("CLOUD_STORAGE_CONFIG", "云存储配置"),
    /**
     * 百度推送地址
     */
    BAIDU_PUSH_URL("BAIDU_PUSH_URL", "百度推送地址"),
    /**
     * 网站名称
     */
    SITE_NAME("SITE_NAME", "网站名称"),
    /**
     * 网站描述
     */
    SITE_DESC("SITE_DESC", "网站描述"),
    /**
     * 网站关键字
     */
    SITE_KWD("SITE_KWD", "网站关键字"),
    /**
     * 站长名称
     */
    SITE_PERSON_NAME("SITE_PERSON_NAME", "站长名称"),
    /**
     * 站长描述
     */
    SITE_PERSON_DESC("SITE_PERSON_DESC", "站长描述"),
    /**
     * 站长头像
     */
    SITE_PERSON_PIC("SITE_PERSON_PIC", "站长头像"),
    ;

    private final String value;
    private final String describe;

}