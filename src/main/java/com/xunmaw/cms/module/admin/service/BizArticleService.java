package com.xunmaw.cms.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xunmaw.cms.module.admin.model.BizArticle;
import com.xunmaw.cms.module.admin.vo.ArticleConditionVo;

import java.util.List;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
public interface BizArticleService extends IService<BizArticle> {

    /**
     * 分页查询
     *
     * @param vo
     * @return
     */
    List<BizArticle> findByCondition(IPage<BizArticle> page, ArticleConditionVo vo);

    /**
     * 首页轮播
     *
     * @return
     */
    List<BizArticle> sliderList();

    /**
     * 站长推荐
     *
     * @param pageSize
     * @return
     */
    List<BizArticle> recommendedList(int pageSize);

    /**
     * 最近文章
     *
     * @param pageSize
     * @return
     */

    List<BizArticle> recentList(int pageSize);

    /**
     * 随机文章
     *
     * @param pageSize
     * @return
     */
    List<BizArticle> randomList(int pageSize);

    /**
     * 热门文章
     *
     * @param pageSize
     * @return
     */
    List<BizArticle> hotList(int pageSize);

    /**
     * 根据id获取文章
     *
     * @param id
     * @return
     */
    BizArticle selectById(Integer id);

    /**
     * 插入
     *
     * @return
     */
    BizArticle insertArticle(BizArticle bizArticle);

    /**
     * 批量删除文章
     *
     * @param ids
     * @return
     */
    int deleteBatch(Integer[] ids);

    /**
     * 根据categoryId获取文章列表
     *
     * @param categoryId
     * @return
     */
    List<BizArticle> selectByCategoryId(Integer categoryId);


}
