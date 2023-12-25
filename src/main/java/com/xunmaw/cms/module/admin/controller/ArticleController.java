package com.xunmaw.cms.module.admin.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xunmaw.cms.common.util.CoreConst;
import com.xunmaw.cms.common.util.Pagination;
import com.xunmaw.cms.common.util.PushArticleUtil;
import com.xunmaw.cms.common.util.ResultUtil;
import com.xunmaw.cms.enums.SysConfigKey;
import com.xunmaw.cms.module.admin.model.BizArticle;
import com.xunmaw.cms.module.admin.model.BizCategory;
import com.xunmaw.cms.module.admin.model.BizTags;
import com.xunmaw.cms.module.admin.model.User;
import com.xunmaw.cms.module.admin.service.*;
import com.xunmaw.cms.module.admin.vo.ArticleConditionVo;
import com.xunmaw.cms.module.admin.vo.BaiduPushResVo;
import com.xunmaw.cms.module.admin.vo.base.PageResultVo;
import com.xunmaw.cms.module.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台文章管理
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Controller
@RequestMapping("article")
@AllArgsConstructor
public class ArticleController {

    private final BizArticleService articleService;
    private final BizArticleTagsService articleTagsService;
    private final BizCategoryService categoryService;
    private final BizTagsService tagsService;
    private final SysConfigService configService;

    @PostMapping("list")
    @ResponseBody
    public PageResultVo loadArticle(ArticleConditionVo articleConditionVo, Integer pageNumber, Integer pageSize) {
        articleConditionVo.setSliderFlag(true);
        IPage<BizArticle> page = new Pagination<>(pageNumber, pageSize);
        List<BizArticle> articleList = articleService.findByCondition(page, articleConditionVo);
        return ResultUtil.table(articleList, page.getTotal());
    }

    /*文章*/
    @GetMapping("/add")
    public String addArticle(Model model) {
        BizCategory bizCategory = new BizCategory();
        bizCategory.setStatus(CoreConst.STATUS_VALID);
        List<BizCategory> bizCategories = categoryService.selectCategories(bizCategory);
        List<BizTags> tags = tagsService.list();
        model.addAttribute("categories", bizCategories);
        model.addAttribute("tags", tags);
        BizArticle bizArticle = new BizArticle().setTags(new ArrayList<>()).setOriginal(1).setSlider(0).setTop(0).setRecommended(0).setComment(1);
        model.addAttribute("article", bizArticle);
        return CoreConst.ADMIN_PREFIX + "article/publish";
    }

    @PostMapping("/add")
    @ResponseBody
    @Transactional
    @CacheEvict(value = "article", allEntries = true)
    public ResponseVo add(BizArticle bizArticle, Integer[] tag) {
        try {
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            bizArticle.setUserId(user.getUserId());
            bizArticle.setAuthor(user.getNickname());
            BizArticle article = articleService.insertArticle(bizArticle);
            articleTagsService.insertList(tag, article.getId());
            return ResultUtil.success("保存文章成功");
        } catch (Exception e) {
            return ResultUtil.error("保存文章失败");
        }
    }

    @GetMapping("/edit")
    public String edit(Model model, Integer id) {
        BizArticle bizArticle = articleService.selectById(id);
        model.addAttribute("article", bizArticle);
        BizCategory bizCategory = new BizCategory();
        bizCategory.setStatus(CoreConst.STATUS_VALID);
        List<BizCategory> bizCategories = categoryService.selectCategories(bizCategory);
        model.addAttribute("categories", bizCategories);
        List<BizTags> sysTags = tagsService.list();
        /*方便前端处理回显*/
        List<BizTags> aTags = new ArrayList<>();
        List<BizTags> sTags = new ArrayList<>();
        boolean flag;
        for (BizTags sysTag : sysTags) {
            flag = false;
            for (BizTags articleTag : bizArticle.getTags()) {
                if (articleTag.getId().equals(sysTag.getId())) {
                    BizTags tempTags = new BizTags();
                    tempTags.setId(sysTag.getId());
                    tempTags.setName(sysTag.getName());
                    aTags.add(tempTags);
                    sTags.add(tempTags);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                sTags.add(sysTag);
            }
        }
        bizArticle.setTags(aTags);
        model.addAttribute("tags", sTags);
        return CoreConst.ADMIN_PREFIX + "article/publish";
    }

    @PostMapping("/edit")
    @ResponseBody
    @CacheEvict(value = "article", allEntries = true)
    public ResponseVo edit(BizArticle article, Integer[] tag) {
        articleService.updateById(article);
        articleTagsService.removeByArticleId(article.getId());
        articleTagsService.insertList(tag, article.getId());
        return ResultUtil.success("编辑文章成功");
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo delete(Integer id) {
        return deleteBatch(new Integer[]{id});
    }

    @PostMapping("/batch/delete")
    @ResponseBody
    public ResponseVo deleteBatch(@RequestParam("ids[]") Integer[] ids) {
        int i = articleService.deleteBatch(ids);
        if (i > 0) {
            return ResultUtil.success("删除文章成功");
        } else {
            return ResultUtil.error("删除文章失败");
        }
    }

    @PostMapping("/batch/push")
    @ResponseBody
    public ResponseVo pushBatch(@RequestParam("urls[]") String[] urls) {
        try {
            String url = configService.selectAll().get(SysConfigKey.BAIDU_PUSH_URL.getValue());
            BaiduPushResVo baiduPushResVo = JSON.parseObject(PushArticleUtil.postBaidu(url, urls), BaiduPushResVo.class);
            if (baiduPushResVo.getNotSameSite() == null && baiduPushResVo.getNotValid() == null) {
                return ResultUtil.success("推送文章成功");
            } else {
                return ResultUtil.error("推送文章失败", baiduPushResVo);
            }
        } catch (Exception e) {
            return ResultUtil.error("推送文章失败,请检查百度推送接口！");
        }

    }
}
