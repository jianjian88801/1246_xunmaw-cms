package com.xunmaw.cms.module.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xunmaw.cms.common.util.CoreConst;
import com.xunmaw.cms.common.util.Pagination;
import com.xunmaw.cms.common.util.ResultUtil;
import com.xunmaw.cms.module.admin.model.BizTheme;
import com.xunmaw.cms.module.admin.service.BizThemeService;
import com.xunmaw.cms.module.admin.vo.base.PageResultVo;
import com.xunmaw.cms.module.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 后台主题配置
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Controller
@RequestMapping("theme")
@AllArgsConstructor
public class ThemeController {

    private final BizThemeService bizThemeService;


    @PostMapping("list")
    @ResponseBody
    public PageResultVo loadTheme(Integer pageNumber, Integer pageSize) {
        IPage<BizTheme> page = new Pagination<>(pageNumber, pageSize);
        page = bizThemeService.page(page);
        return ResultUtil.table(page.getRecords(), page.getTotal());
    }

    @GetMapping("/add")
    public String add() {
        return CoreConst.ADMIN_PREFIX + "theme/form";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseVo add(BizTheme bizTheme) {
        Date date = new Date();
        bizTheme.setCreateTime(date);
        bizTheme.setUpdateTime(date);
        bizTheme.setStatus(CoreConst.STATUS_INVALID);
        boolean i = bizThemeService.save(bizTheme);
        if (i) {
            return ResultUtil.success("新增主题成功");
        } else {
            return ResultUtil.error("新增主题失败");
        }
    }

    @GetMapping("/edit")
    public String edit(Model model, Integer id) {
        BizTheme bizTheme = bizThemeService.getById(id);
        model.addAttribute("theme", bizTheme);
        return CoreConst.ADMIN_PREFIX + "theme/form";
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseVo edit(BizTheme bizTheme) {
        bizTheme.setUpdateTime(new Date());
        boolean i = bizThemeService.updateById(bizTheme);
        if (i) {
            return ResultUtil.success("编辑主题成功");
        } else {
            return ResultUtil.error("编辑主题失败");
        }
    }

    @PostMapping("/use")
    @ResponseBody
    public ResponseVo use(Integer id) {
        int i = bizThemeService.useTheme(id);
        if (i > 0) {
            return ResultUtil.success("启用主题成功");
        } else {
            return ResultUtil.error("启用主题失败");
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo delete(Integer id) {
        boolean i = bizThemeService.removeById(id);
        if (i) {
            return ResultUtil.success("删除主题成功");
        } else {
            return ResultUtil.error("删除主题失败");
        }
    }

    @PostMapping("/batch/delete")
    @ResponseBody
    public ResponseVo deleteBatch(@RequestParam("ids[]") Integer[] ids) {
        int i = bizThemeService.deleteBatch(ids);
        if (i > 0) {
            return ResultUtil.success("删除主题成功");
        } else {
            return ResultUtil.error("删除主题失败");
        }
    }

}
