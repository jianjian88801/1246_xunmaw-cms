package com.xunmaw.cms.module.admin.controller;

import com.xunmaw.cms.common.util.CoreConst;
import com.xunmaw.cms.common.util.ResultUtil;
import com.xunmaw.cms.module.admin.service.StaticHtmlService;
import com.xunmaw.cms.module.admin.service.SysConfigService;
import com.xunmaw.cms.module.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.xunmaw.cms.common.util.CoreConst.SITE_STATIC_KEY;

/**
 * 后台网站信息配置
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Controller
@AllArgsConstructor
public class SiteInfoController {

    private final SysConfigService configService;
    private final StaticHtmlService staticHtmlService;

    @PostMapping("/siteinfo/edit")
    @ResponseBody
    public ResponseVo save(@RequestParam Map<String, String> map, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (map.containsKey(SITE_STATIC_KEY)) {
                boolean siteStaticOn = "on".equalsIgnoreCase(map.get(SITE_STATIC_KEY));
                if (siteStaticOn) {
                    staticHtmlService.makeStaticSite(request, response, true);
                }
                CoreConst.SITE_STATIC.set(siteStaticOn);
            }
            configService.updateAll(map, request, response);
            return ResultUtil.success("保存网站信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("保存网站信息失败");
        }
    }
}
