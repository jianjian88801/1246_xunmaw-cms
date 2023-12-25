package com.xunmaw.cms.common.util;

import lombok.experimental.UtilityClass;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 常量工具类
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@UtilityClass
public class CoreConst {

    public static final Integer SUCCESS_CODE = 200;
    public static final Integer FAIL_CODE = 500;
    public static final Integer STATUS_VALID = 1;
    public static final Integer STATUS_INVALID = 0;
    public static final Integer PAGE_SIZE = 10;
    public static final Integer TOP_MENU_ID = 0;
    public static final String TOP_MENU_NAME = "顶层菜单";
    public static final String SHIRO_REDIS_SESSION_PREFIX = "pb_cms:session:";
    public static final String SHIRO_REDIS_CACHE_NAME = "shiro_pb_cms";

    public static final String ADMIN_PREFIX = "admin/";

    public static final String THEME_PREFIX = "theme/";

    /**
     * 文件存储服务类型
     * 1：七牛  2：阿里云  3：腾讯云  4：本地
     */
    public static final int UPLOAD_TYPE_QINIUYUN = 1;
    public static final int UPLOAD_TYPE_ALIYUN = 2;
    public static final int UPLOAD_TYPE_QCLOUD = 3;
    public static final int UPLOAD_TYPE_LOCAL = 4;

    /**
     * 是否启用演示模拟数据
     */
    public static final boolean ENABLE_DEMO_DATA = true;

    /**
     * 网站是否静态化
     */
    public static final String SITE_STATIC_KEY = "SITE_STATIC";
    public static final AtomicBoolean SITE_STATIC = new AtomicBoolean(false);
}
