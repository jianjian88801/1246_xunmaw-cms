package com.xunmaw.cms.module.admin.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface StaticHtmlService {

    /**
     * 生成全站静态页面，包含首页、分类列表、标签列表、文字详情
     * @param request
     * @param response
     * @param force 是否强制重新生成所有页面
     */
    void makeStaticSite(HttpServletRequest request, HttpServletResponse response, Boolean force);

    void createIndexHtml(HttpServletRequest request, HttpServletResponse response, Boolean force);

    void createArticleHtml(HttpServletRequest request, HttpServletResponse response, Boolean force);

    void createCommmentHtml(HttpServletRequest request, HttpServletResponse response, Boolean force);

    void createCategoryHtml(HttpServletRequest request, HttpServletResponse response, Boolean force);

    void createTagHtml(HttpServletRequest request, HttpServletResponse response, Boolean force);
}
