package com.xunmaw.cms.module.admin.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xunmaw.cms.common.config.properties.StaticizeProperties;
import com.xunmaw.cms.common.util.CoreConst;
import com.xunmaw.cms.common.util.Pagination;
import com.xunmaw.cms.component.CommonDataService;
import com.xunmaw.cms.module.admin.model.BizArticle;
import com.xunmaw.cms.module.admin.model.BizArticleTags;
import com.xunmaw.cms.module.admin.model.BizCategory;
import com.xunmaw.cms.module.admin.model.BizTags;
import com.xunmaw.cms.module.admin.service.*;
import com.xunmaw.cms.module.admin.vo.ArticleConditionVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class StaticHtmlServiceImpl implements StaticHtmlService {

    private final StaticizeProperties staticizeProperties;
    private final TemplateEngine templateEngine;
    private final BizArticleService bizArticleService;
    private final BizCategoryService bizCategoryService;
    private final BizTagsService bizTagsService;
    private final BizArticleTagsService bizArticleTagsService;
    private final BizThemeService bizThemeService;
    private final CommonDataService commonDataService;

    @Override
    public void makeStaticSite(HttpServletRequest request, HttpServletResponse response, Boolean force) {
        createIndexHtml(request, response, true);
        createArticleHtml(request, response, true);
        createCategoryHtml(request, response, true);
        createTagHtml(request, response, true);
        createCommmentHtml(request, response, true);
    }

    @Override
    public void createIndexHtml(HttpServletRequest request, HttpServletResponse response, Boolean force) {
        List<BizArticle> sliderList = bizArticleService.sliderList();
        ArticleConditionVo vo = new ArticleConditionVo();
        vo.setStatus(CoreConst.STATUS_VALID);
        long total = bizArticleService.count(Wrappers.<BizArticle>lambdaQuery().eq(BizArticle::getStatus, CoreConst.STATUS_VALID));
        if (total == 0) {
            Map<String, Object> paramMap = MapUtil.<String, Object>builder()
                    .put("pageUrl", "blog/index")
                    .put("categoryId", "index")
                    .put("sliderList", sliderList)
                    .put("page", new Pagination<>(1, 10))
                    .put("articleList", Collections.emptyList())
                    .build();
            createHtml(request, response, force, paramMap, "index", "index");
            return;
        }
        IPage<Object> pagination = new Pagination<>(vo.getPageNumber(), vo.getPageSize()).setTotal(total);
        for (long pageNum = 1; pageNum <= pagination.getPages(); pageNum++) {
            IPage<BizArticle> page = new Pagination<>(vo.getPageNumber(), vo.getPageSize());
            List<BizArticle> articleList = bizArticleService.findByCondition(page, vo);
            Map<String, Object> paramMap = MapUtil.<String, Object>builder()
                    .put("pageUrl", "blog/index")
                    .put("categoryId", "index")
                    .put("sliderList", sliderList)
                    .put("page", page)
                    .put("articleList", articleList)
                    .build();
            if (pageNum == 1) {
                createHtml(request, response, force, paramMap, "index", "index");
            }
            createHtml(request, response, force, paramMap, "index", "index" + File.separator + pageNum);
        }
    }

    @Override
    public void createArticleHtml(HttpServletRequest request, HttpServletResponse response, Boolean force) {
        List<BizArticle> articleList = bizArticleService.findByCondition(new Pagination<>(1, 99999), new ArticleConditionVo().setStatus(CoreConst.STATUS_VALID));
        for (BizArticle article : articleList) {
            Map<String, Object> paramMap = MapUtil.newHashMap(2);
            paramMap.put("article", article);
            paramMap.put("categoryId", article.getCategoryId());
            createHtml(request, response, force, paramMap, "article", String.valueOf(article.getId()));
        }
    }

    @Override
    public void createCommmentHtml(HttpServletRequest request, HttpServletResponse response, Boolean force) {
        createHtml(request, response, force, Collections.emptyMap(), "comment", "comment");
    }

    @Override
    public void createCategoryHtml(HttpServletRequest request, HttpServletResponse response, Boolean force) {
        List<BizCategory> categoryList = bizCategoryService.list(Wrappers.<BizCategory>lambdaQuery().eq(BizCategory::getStatus, CoreConst.STATUS_VALID));
        for (BizCategory category : categoryList) {

            ArticleConditionVo vo = new ArticleConditionVo();
            vo.setStatus(CoreConst.STATUS_VALID);
            vo.setCategoryId(category.getId());

            long total = bizArticleService.count(Wrappers.<BizArticle>lambdaQuery().eq(BizArticle::getCategoryId, category.getId()).eq(BizArticle::getStatus, CoreConst.STATUS_VALID));
            if (total == 0) {
                Map<String, Object> paramMap = MapUtil.<String, Object>builder()
                        .put("pageUrl", "blog/category/" + category.getId())
                        .put("categoryId", category.getId())
                        .put("categoryName", category.getName())
                        .put("articleList", Collections.emptyList())
                        .build();
                createHtml(request, response, force, paramMap, "index", "category" + File.separator + category.getId());
                continue;
            }
            IPage<Object> pagination = new Pagination<>(vo.getPageNumber(), vo.getPageSize()).setTotal(total);
            for (long pageNum = 1; pageNum <= pagination.getPages(); pageNum++) {

                Pagination<BizArticle> page = new Pagination<>(pageNum, vo.getPageSize());
                List<BizArticle> articleList = bizArticleService.findByCondition(page, vo);
                Map<String, Object> paramMap = MapUtil.<String, Object>builder()
                        .put("pageUrl", "blog/category/" + category.getId())
                        .put("categoryId", category.getId())
                        .put("categoryName", category.getName())
                        .put("page", page)
                        .put("articleList", articleList)
                        .build();
                if (pageNum == 1) {
                    createHtml(request, response, force, paramMap, "index", "category" + File.separator + category.getId());
                }
                createHtml(request, response, force, paramMap, "index", "category" + File.separator + category.getId() + File.separator + pageNum);
            }
        }
    }

    @Override
    public void createTagHtml(HttpServletRequest request, HttpServletResponse response, Boolean force) {
        List<BizTags> tagsList = bizTagsService.list();
        for (BizTags tag : tagsList) {

            ArticleConditionVo vo = new ArticleConditionVo();
            vo.setTagId(tag.getId());

            long total = bizArticleTagsService.count(Wrappers.<BizArticleTags>lambdaQuery().eq(BizArticleTags::getTagId, tag.getId()));
            if (total == 0) {
                Map<String, Object> paramMap = MapUtil.<String, Object>builder()
                        .put("pageUrl", "blog/tag/" + tag.getId())
                        .put("articleList", Collections.emptyList())
                        .build();
                createHtml(request, response, force, paramMap, "index", "tag" + File.separator + tag.getId());
                continue;
            }
            IPage<Object> pagination = new Pagination<>(vo.getPageNumber(), vo.getPageSize()).setTotal(total);
            for (long pageNum = 1; pageNum <= pagination.getPages(); pageNum++) {
                Pagination<BizArticle> page = new Pagination<>(pageNum, vo.getPageSize());
                List<BizArticle> articleList = bizArticleService.findByCondition(page, vo);
                Map<String, Object> paramMap = MapUtil.<String, Object>builder()
                        .put("pageUrl", "blog/tag/" + tag.getId())
                        .put("page", page)
                        .put("articleList", articleList)
                        .build();
                if (pageNum == 1) {
                    createHtml(request, response, force, paramMap, "index", "tag" + File.separator + tag.getId());
                }
                createHtml(request, response, force, paramMap, "index", "tag" + File.separator + tag.getId() + File.separator + pageNum);
            }
        }
    }


    public void createHtml(HttpServletRequest request, HttpServletResponse response, Boolean force, Map<String, Object> map, String templateUrl, String fileName) {
        if (StrUtil.isBlank(staticizeProperties.getFolder())) {
            throw new IllegalArgumentException("请先在Yml配置静态页面生成路径");
        }
        log.info("开始生成静态页面: {}", fileName);

        templateUrl = StrUtil.removePrefix(templateUrl, File.separator);

        PrintWriter writer = null;
        try {
            Map<String, Object> paramMap = commonDataService.getAllCommonData();
            // 获取页面数据
            paramMap.putAll(map);
            // 创建thymeleaf上下文对象
            WebContext context = new WebContext(request, response, request.getServletContext());
            // 把数据放入上下文对象
            context.setVariables(paramMap);
            // 文件生成路径
            String fileUrl = StrUtil.appendIfMissing(staticizeProperties.getFolder(), File.separator) + templateUrl + File.separator + fileName + ".html";
            // 自动创建上层文件夹
            File directory = new File(StrUtil.subBefore(fileUrl, File.separator, true));
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // 创建输出流
            File file = new File(fileUrl);
            if (Boolean.FALSE.equals(force) && file.exists()) {
                // 不强制覆盖现有文件
                return;
            }
            writer = new PrintWriter(file);
            // 执行页面静态化方法
            templateEngine.process(CoreConst.THEME_PREFIX + bizThemeService.selectCurrent().getName() + File.separator + templateUrl, context, writer);
        } catch (Exception e) {
            log.error("页面静态化出错：{}", e.getMessage(), e);
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }


}
