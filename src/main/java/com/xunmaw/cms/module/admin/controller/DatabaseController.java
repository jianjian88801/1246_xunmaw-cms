package com.xunmaw.cms.module.admin.controller;

import com.xunmaw.cms.common.util.CoreConst;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 后台SQL监控
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Controller
@RequestMapping("/database")
public class DatabaseController {
    @GetMapping(value = "/monitoring")
    public ModelAndView databaseMonitoring() {
        return new ModelAndView(CoreConst.ADMIN_PREFIX + "database/monitoring");
    }
}
