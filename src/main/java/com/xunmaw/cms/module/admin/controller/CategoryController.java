package com.xunmaw.cms.module.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.xunmaw.cms.common.util.CoreConst;
import com.xunmaw.cms.common.util.ResultUtil;
import com.xunmaw.cms.module.admin.model.BizArticle;
import com.xunmaw.cms.module.admin.model.BizCategory;
import com.xunmaw.cms.module.admin.service.BizArticleService;
import com.xunmaw.cms.module.admin.service.BizCategoryService;
import com.xunmaw.cms.module.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.ListUtils;

import java.util.Date;
import java.util.List;

/**
 * 后台类目管理
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Controller
@RequestMapping("category")
@AllArgsConstructor
public class CategoryController {

    private final BizCategoryService categoryService;
    private final BizArticleService articleService;

    @PostMapping("list")
    @ResponseBody
    public List<BizCategory> loadCategory(boolean isFistLevel) {
        BizCategory bizCategory = new BizCategory();
        bizCategory.setStatus(CoreConst.STATUS_VALID);
        if (isFistLevel) {
            bizCategory.setPid(CoreConst.TOP_MENU_ID);
        }
        return categoryService.selectCategories(bizCategory);
    }

    @GetMapping("/add")
    public String add() {
        return CoreConst.ADMIN_PREFIX + "category/form";
    }

    @PostMapping("/add")
    @ResponseBody
    @CacheEvict(value = "category", allEntries = true)
    public ResponseVo add(BizCategory bizCategory) {
        if (!CoreConst.TOP_MENU_ID.equals(bizCategory.getPid())) {
            List<BizArticle> bizArticles = articleService.selectByCategoryId(bizCategory.getPid());
            if (!ListUtils.isEmpty(bizArticles)) {
                return ResultUtil.error("添加失败，父级分类下存在文章");
            }
        }
        Date date = new Date();
        bizCategory.setCreateTime(date);
        bizCategory.setUpdateTime(date);
        bizCategory.setStatus(CoreConst.STATUS_VALID);
        boolean i = categoryService.save(bizCategory);
        if (i) {
            return ResultUtil.success("新增分类成功");
        } else {
            return ResultUtil.error("新增分类失败");
        }
    }

    @GetMapping("/edit")
    public String edit(Model model, Integer id) {
        BizCategory bizCategory = categoryService.selectById(id);
        model.addAttribute("category", bizCategory);
        return CoreConst.ADMIN_PREFIX + "category/form";
    }

    @PostMapping("/edit")
    @ResponseBody
    @CacheEvict(value = "category", allEntries = true)
    public ResponseVo edit(BizCategory bizCategory) {
        bizCategory.setUpdateTime(new Date());
        boolean i = categoryService.updateById(bizCategory);
        if (i) {
            return ResultUtil.success("编辑分类成功");
        } else {
            return ResultUtil.error("编辑分类失败");
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo delete(Integer id) {
        if (CollUtil.isNotEmpty(categoryService.selectByPid(id))) {
            return ResultUtil.error("该分类下存在子分类！");
        }
        return deleteBatch(new Integer[]{id});
    }

    @PostMapping("/batch/delete")
    @ResponseBody
    public ResponseVo deleteBatch(@RequestParam("ids[]") Integer[] ids) {
        int i = categoryService.deleteBatch(ids);
        if (i > 0) {
            return ResultUtil.success("删除分类成功");
        } else {
            return ResultUtil.error("删除分类失败");
        }
    }

}
