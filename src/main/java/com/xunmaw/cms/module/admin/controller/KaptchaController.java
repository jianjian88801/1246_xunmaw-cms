package com.xunmaw.cms.module.admin.controller;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.utils.CaptchaUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 获取验证码图片
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Slf4j
@Controller
@AllArgsConstructor
public class KaptchaController {


    /**
     * 获取验证码图片
     * Gets captcha code.
     *
     * @param request  the request
     * @param response the response
     * @throws IOException the io exception
     */
    @RequestMapping("/verificationCode")
    public void getCaptchaCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        captcha.setLen(2);
        CaptchaUtil.out(captcha, request, response);
    }
}
