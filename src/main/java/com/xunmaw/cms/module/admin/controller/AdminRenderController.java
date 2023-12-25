package com.xunmaw.cms.module.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xunmaw.cms.common.util.CoreConst;
import com.xunmaw.cms.module.admin.model.BizCategory;
import com.xunmaw.cms.module.admin.model.User;
import com.xunmaw.cms.module.admin.service.BizCategoryService;
import com.xunmaw.cms.module.admin.service.BizStatisticService;
import com.xunmaw.cms.module.admin.service.PermissionService;
import com.xunmaw.cms.module.admin.service.SysConfigService;
import lombok.AllArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * 后台管理页面跳转控制器
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Controller
@AllArgsConstructor
public class AdminRenderController {


    private final BizCategoryService categoryService;
    private final SysConfigService sysConfigService;
    private final PermissionService permissionService;
    private final BizStatisticService statisticService;

    /**
     * 后台首页
     */
    @RequestMapping("/admin")
    public String index(Model model) {
        model.addAttribute("menuTree", permissionService.selectMenuTreeByUserId(((User) SecurityUtils.getSubject().getPrincipal()).getUserId()));
        return CoreConst.ADMIN_PREFIX + "index/index";
    }

    /**
     * 工作台
     */
    @GetMapping("/workdest")
    public String workdest(Model model) {
        model.addAttribute("statistic", statisticService.indexStatistic());
        return CoreConst.ADMIN_PREFIX + "index/dashboard";
    }

    /**
     * 用户列表入口
     */
    @GetMapping("/users")
    public String userList() {
        return CoreConst.ADMIN_PREFIX + "user/list";
    }

    /**
     * 角色列表入口
     */
    @GetMapping("/roles")
    public String roleList() {
        return CoreConst.ADMIN_PREFIX + "role/list";
    }

    /**
     * 权限列表入口
     */
    @GetMapping("/permissions")
    public String permissionList() {
        return CoreConst.ADMIN_PREFIX + "permission/list";
    }

    /**
     * 在线用户入口
     */
    @GetMapping("/online/users")
    public String onlineUsers() {
        return CoreConst.ADMIN_PREFIX + "onlineUsers/list";
    }

    /**
     * 网站基本信息
     *
     * @param model
     */
    @GetMapping("/siteinfo")
    public String siteinfo(Model model) {
        Map<String, String> map = sysConfigService.selectAll();
        model.addAttribute("siteinfo", map);
        return CoreConst.ADMIN_PREFIX + "site/siteinfo";
    }

    /**
     * 友情链接
     */
    @GetMapping("/links")
    public String links() {
        return CoreConst.ADMIN_PREFIX + "link/list";
    }

    /**
     * 分类
     */
    @GetMapping("/categories")
    public String categories() {
        return CoreConst.ADMIN_PREFIX + "category/list";
    }

    /**
     * 标签
     */
    @GetMapping("/tags")
    public String tags() {
        return CoreConst.ADMIN_PREFIX + "tag/list";
    }

    /**
     * 文章
     *
     * @param model
     */
    @GetMapping("/articles")
    public String articles(Model model) {
        List<BizCategory> categories = categoryService.list(Wrappers.<BizCategory>lambdaQuery().eq(BizCategory::getStatus, CoreConst.STATUS_VALID));
        model.addAttribute("categories", categories);
        return CoreConst.ADMIN_PREFIX + "article/list";
    }

    /**
     * 评论
     */
    @GetMapping("/comments")
    public String comments() {
        return CoreConst.ADMIN_PREFIX + "comment/list";
    }

    /**
     * 主题
     */
    @GetMapping("themes")
    public String themes() {
        return CoreConst.ADMIN_PREFIX + "theme/list";
    }

}
