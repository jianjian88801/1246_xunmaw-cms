package com.xunmaw.cms.component;

import cn.hutool.core.map.MapUtil;
import com.xunmaw.cms.common.util.CoreConst;
import com.xunmaw.cms.module.admin.model.BizCategory;
import com.xunmaw.cms.module.admin.model.BizLink;
import com.xunmaw.cms.module.admin.model.BizTags;
import com.xunmaw.cms.module.admin.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * thymeleaf调用后台的工具类
 *
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Slf4j
@Component("commonDataService")
@AllArgsConstructor
public class CommonDataService {

    private final BizCategoryService bizCategoryService;
    private final BizArticleService bizArticleService;
    private final BizTagsService bizTagsService;
    private final BizLinkService bizLinkService;
    private final BizSiteInfoService siteInfoService;
    private final SysConfigService sysConfigService;

    public Object get(String moduleName) {
        try {
            DataTypeEnum dataTypeEnum = DataTypeEnum.valueOf(moduleName);
            switch (dataTypeEnum) {
                case CATEGORY_LIST:
                    BizCategory bizCategory = new BizCategory();
                    bizCategory.setStatus(CoreConst.STATUS_VALID);
                    return bizCategoryService.selectCategories(bizCategory);
                case TAG_LIST:
                    return bizTagsService.selectTags(new BizTags());
                case SLIDER_LIST:
                    return bizArticleService.sliderList();
                case RECENT_LIST:
                    return bizArticleService.recentList(CoreConst.PAGE_SIZE);
                case RECOMMENDED_LIST:
                    return bizArticleService.recommendedList(CoreConst.PAGE_SIZE);
                case HOT_LIST:
                    return bizArticleService.hotList(CoreConst.PAGE_SIZE);
                case RANDOM_LIST:
                    return bizArticleService.randomList(CoreConst.PAGE_SIZE);
                case LINK_LIST:
                    BizLink bizLink = new BizLink();
                    bizLink.setStatus(CoreConst.STATUS_VALID);
                    return bizLinkService.selectLinks(bizLink);
                case SITE_INFO:
                    return siteInfoService.getSiteInfo();
                case SITE_CONFIG:
                    return sysConfigService.selectAll();
                default:
                    return null;
            }
        } catch (Exception e) {
            log.error("获取网站公共信息[{}]发生异常: {}", moduleName, e.getMessage(), e);
        }
        return null;
    }

    public Map<String, Object> getAllCommonData() {
        Map<String, Object> result = MapUtil.newHashMap(DataTypeEnum.values().length);
        for (DataTypeEnum dataTypeEnum : DataTypeEnum.values()) {
            result.put(dataTypeEnum.name(), get(dataTypeEnum.name()));
        }
        return result;
    }

    private enum DataTypeEnum {
        // 分类
        CATEGORY_LIST,
        // 标签
        TAG_LIST,
        //轮播文章
        SLIDER_LIST,
        //最近文章
        RECENT_LIST,
        //推荐文章
        RECOMMENDED_LIST,
        //热门文章
        HOT_LIST,
        //随机文章
        RANDOM_LIST,
        //友链
        LINK_LIST,
        //网站信息统计
        SITE_INFO,
        //网站基本信息配置
        SITE_CONFIG
    }
}
