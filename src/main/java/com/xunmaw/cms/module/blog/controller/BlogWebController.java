package com.xunmaw.cms.module.blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xunmaw.cms.common.util.CoreConst;
import com.xunmaw.cms.common.util.Pagination;
import com.xunmaw.cms.exception.ArticleNotFoundException;
import com.xunmaw.cms.module.admin.model.BizArticle;
import com.xunmaw.cms.module.admin.model.BizCategory;
import com.xunmaw.cms.module.admin.service.BizArticleService;
import com.xunmaw.cms.module.admin.service.BizCategoryService;
import com.xunmaw.cms.module.admin.service.BizThemeService;
import com.xunmaw.cms.module.admin.vo.ArticleConditionVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * CMS页面相关接口
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Controller
@AllArgsConstructor
public class BlogWebController {

    private final BizArticleService bizArticleService;
    private final BizCategoryService categoryService;
    private final BizThemeService bizThemeService;

    /**
     * 首页
     *
     * @param model
     * @param vo
     * @return
     */
    @GetMapping({"/", "/blog/index/{pageNumber}"})
    public String index(@PathVariable(value = "pageNumber", required = false) Integer pageNumber,
                        ArticleConditionVo vo,
                        Model model) {
        if (CoreConst.SITE_STATIC.get()) {
            return "forward:/html/index/index.html";
        }
        if (pageNumber != null) {
            vo.setPageNumber(pageNumber);
        } else {
            model.addAttribute("sliderList", bizArticleService.sliderList());//轮播文章
        }
        model.addAttribute("pageUrl", "blog/index");
        model.addAttribute("categoryId", "index");
        loadMainPage(model, vo);
        return CoreConst.THEME_PREFIX + bizThemeService.selectCurrent().getName() + "/index";
    }

    /**
     * 分类列表
     *
     * @param categoryId
     * @param pageNumber
     * @param model
     * @return
     */
    @GetMapping({"/blog/category/{categoryId}", "/blog/category/{categoryId}/{pageNumber}"})
    public String category(@PathVariable("categoryId") Integer categoryId,
                           @PathVariable(value = "pageNumber", required = false) Integer pageNumber,
                           Model model) {
        if (CoreConst.SITE_STATIC.get()) {
            return "forward:/html/index/category/"+ (pageNumber == null ? categoryId : categoryId + "/" + pageNumber)  +".html";
        }
        ArticleConditionVo vo = new ArticleConditionVo();
        vo.setCategoryId(categoryId);
        if (pageNumber != null) {
            vo.setPageNumber(pageNumber);
        }
        model.addAttribute("pageUrl", "blog/category/" + categoryId);
        model.addAttribute("categoryId", categoryId);
        loadMainPage(model, vo);
        return CoreConst.THEME_PREFIX + bizThemeService.selectCurrent().getName() + "/index";
    }


    /**
     * 标签列表
     *
     * @param tagId
     * @param model
     * @return
     */
    @GetMapping({"/blog/tag/{tagId}", "/blog/tag/{tagId}/{pageNumber}"})
    public String tag(@PathVariable("tagId") Integer tagId,
                      @PathVariable(value = "pageNumber", required = false) Integer pageNumber,
                      Model model) {
        if (CoreConst.SITE_STATIC.get()) {
            return "forward:/html/index/tag/"+ (pageNumber == null ? tagId : tagId + "/" + pageNumber)  +".html";
        }
        ArticleConditionVo vo = new ArticleConditionVo();
        vo.setTagId(tagId);
        if (pageNumber != null) {
            vo.setPageNumber(pageNumber);
        }
        model.addAttribute("pageUrl", "blog/tag/" + tagId);
        loadMainPage(model, vo);
        return CoreConst.THEME_PREFIX + bizThemeService.selectCurrent().getName() + "/index";
    }

    /**
     * 文章详情
     *
     * @param model
     * @param articleId
     * @return
     */
    @GetMapping("/blog/article/{articleId}")
    public String article(HttpServletRequest request, Model model, @PathVariable("articleId") Integer articleId) {
        if (CoreConst.SITE_STATIC.get()) {
            return "forward:/html/article/" + articleId + ".html";
        }
        BizArticle article = bizArticleService.selectById(articleId);
        if (article == null || CoreConst.STATUS_INVALID.equals(article.getStatus())) {
            throw new ArticleNotFoundException();
        }
        model.addAttribute("article", article);
        model.addAttribute("categoryId", article.getCategoryId());
        return CoreConst.THEME_PREFIX + bizThemeService.selectCurrent().getName() + "/article";
    }

    /**
     * 评论
     *
     * @param model
     * @return
     */
    @GetMapping("/blog/comment")
    public String comment(Model model) {
        if (CoreConst.SITE_STATIC.get()) {
            return "forward:/html/comment/comment.html";
        }
        model.addAttribute("categoryId", "comment");
        return CoreConst.THEME_PREFIX + bizThemeService.selectCurrent().getName() + "/comment";
    }

    private void loadMainPage(Model model, ArticleConditionVo vo) {
        vo.setStatus(CoreConst.STATUS_VALID);
        IPage<BizArticle> page = new Pagination<>(vo.getPageNumber(), vo.getPageSize());
        List<BizArticle> articleList = bizArticleService.findByCondition(page, vo);
        model.addAttribute("page", page);
        model.addAttribute("articleList", articleList);//文章列表
        if (vo.getCategoryId() != null) {
            BizCategory category = categoryService.getById(vo.getCategoryId());
            if (category != null) {
                model.addAttribute("categoryName", category.getName());
            }
        }
    }

}
