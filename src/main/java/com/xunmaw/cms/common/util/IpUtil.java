package com.xunmaw.cms.common.util;

import cn.hutool.http.HtmlUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ip工具类
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Slf4j
@UtilityClass
public class IpUtil {

    public static String getIpAddr(HttpServletRequest request) {
        String ip;
        if (request == null) {
            return null;
        }
        ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(',') > 0) {
                ip = ip.substring(0, ip.indexOf(','));
            }
        }
        if (ip != null) {
            return HtmlUtil.cleanHtmlTag(ip);
        }
        return ip;
    }
}
