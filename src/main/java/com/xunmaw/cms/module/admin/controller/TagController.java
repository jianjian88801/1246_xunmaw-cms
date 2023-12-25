package com.xunmaw.cms.module.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xunmaw.cms.common.util.CoreConst;
import com.xunmaw.cms.common.util.ResultUtil;
import com.xunmaw.cms.module.admin.model.BizTags;
import com.xunmaw.cms.module.admin.service.BizTagsService;
import com.xunmaw.cms.module.admin.vo.base.PageResultVo;
import com.xunmaw.cms.module.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 后台标签配置
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Controller
@RequestMapping("tag")
@AllArgsConstructor
public class TagController {

    private final BizTagsService tagsService;


    @GetMapping("/add")
    public String add() {
        return CoreConst.ADMIN_PREFIX + "tag/form";
    }

    @PostMapping("list")
    @ResponseBody
    public PageResultVo loadTags(BizTags bizTags, Integer pageNumber, Integer pageSize) {
        IPage<BizTags> bizTagsPage = tagsService.pageTags(bizTags, pageNumber, pageSize);
        return ResultUtil.table(bizTagsPage.getRecords(), bizTagsPage.getTotal());
    }

    @PostMapping("/add")
    @ResponseBody
    @CacheEvict(value = "tag", allEntries = true)
    public ResponseVo add(BizTags bizTags) {
        Date date = new Date();
        bizTags.setCreateTime(date);
        bizTags.setUpdateTime(date);
        boolean i = tagsService.save(bizTags);
        if (i) {
            return ResultUtil.success("新增标签成功");
        } else {
            return ResultUtil.error("新增标签失败");
        }
    }

    @GetMapping("/edit")
    public String edit(Model model, Integer id) {
        BizTags bizTags = tagsService.getById(id);
        model.addAttribute("tag", bizTags);
        return CoreConst.ADMIN_PREFIX + "tag/form";
    }

    @PostMapping("/edit")
    @ResponseBody
    @CacheEvict(value = "tag", allEntries = true)
    public ResponseVo edit(BizTags bizTags) {
        bizTags.setUpdateTime(new Date());
        boolean i = tagsService.updateById(bizTags);
        if (i) {
            return ResultUtil.success("编辑标签成功");
        } else {
            return ResultUtil.error("编辑标签失败");
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo delete(Integer id) {
        return deleteBatch(new Integer[]{id});
    }

    @PostMapping("/batch/delete")
    @ResponseBody
    public ResponseVo deleteBatch(@RequestParam("ids[]") Integer[] ids) {
        int i = tagsService.deleteBatch(ids);
        if (i > 0) {
            return ResultUtil.success("删除标签成功");
        } else {
            return ResultUtil.error("删除标签失败");
        }
    }
}
