package com.xunmaw.cms.exception;

import cn.hutool.core.util.StrUtil;
import com.xunmaw.cms.enums.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常统一处理
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandleController {

    @ExceptionHandler(AppException.class)
    public String handleAppException(Exception e, HttpServletRequest request) {
        request.setAttribute("javax.servlet.error.status_code", ResponseStatus.ERROR.getCode());
        Map<String, Object> map = new HashMap<>(2);
        map.put("status", ResponseStatus.ERROR.getCode());
        map.put("msg", StrUtil.isNotBlank(e.getMessage()) ? e.getMessage() : ResponseStatus.ERROR.getMessage());
        log.error("拦截到系统异常AppException: {}", e.getMessage(), e);
        request.setAttribute("ext", map);
        return "forward:/error";
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public String handleArticle(Exception e, HttpServletRequest request) {
        request.setAttribute("javax.servlet.error.status_code", ResponseStatus.NOT_FOUND.getCode());
        return "forward:/error";
    }

    @ExceptionHandler(AuthorizationException.class)
    public String handleAuth(HttpServletRequest request) {
        request.setAttribute("javax.servlet.error.status_code", ResponseStatus.FORBIDDEN.getCode());
        return "forward:/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, HttpServletRequest request) {
        log.error("URI: {} 捕获异常: {}", request.getRequestURI(), e.getMessage(), e);
        request.setAttribute("javax.servlet.error.status_code", ResponseStatus.ERROR.getCode());
        return "forward:/error";
    }


}
