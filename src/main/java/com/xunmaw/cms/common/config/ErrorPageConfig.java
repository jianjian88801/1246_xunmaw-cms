package com.xunmaw.cms.common.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * 配置系统后台出错时跳转到的错误页面
 *
 * @author xunmaw
 * @version V1.0
 * @date 2019年9月11日
 */
@Component
public class ErrorPageConfig implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry errorPageRegistry) {
        ErrorPage e403 = new ErrorPage(HttpStatus.FORBIDDEN, "/error/403");
        ErrorPage e404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404");
        ErrorPage e500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500");
        errorPageRegistry.addErrorPages(e403, e404, e500);
    }

}