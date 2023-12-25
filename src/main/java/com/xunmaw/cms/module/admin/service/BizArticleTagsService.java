package com.xunmaw.cms.module.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xunmaw.cms.module.admin.model.BizArticleTags;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
public interface BizArticleTagsService extends IService<BizArticleTags> {


    /**
     * 通过文章id删除文章-标签关联数据
     *
     * @param articleId
     * @return
     */
    int removeByArticleId(Integer articleId);

    /**
     * 批量添加
     *
     * @param tagIds
     * @param articleId
     */
    void insertList(Integer[] tagIds, Integer articleId);
}
