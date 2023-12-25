package com.xunmaw.cms.module.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunmaw.cms.common.util.CoreConst;
import com.xunmaw.cms.common.util.Pagination;
import com.xunmaw.cms.module.admin.mapper.BizArticleMapper;
import com.xunmaw.cms.module.admin.model.BizArticle;
import com.xunmaw.cms.module.admin.service.BizArticleService;
import com.xunmaw.cms.module.admin.vo.ArticleConditionVo;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Service
@AllArgsConstructor
public class BizArticleServiceImpl extends ServiceImpl<BizArticleMapper, BizArticle> implements BizArticleService {

    private final BizArticleMapper bizArticleMapper;

    @Override
    public List<BizArticle> findByCondition(IPage<BizArticle> page, ArticleConditionVo vo) {
        List<BizArticle> list = bizArticleMapper.findByCondition(page, vo);
        if (CollUtil.isNotEmpty(list)) {
            List<Integer> ids = new ArrayList<>();
            for (BizArticle bizArticle : list) {
                ids.add(bizArticle.getId());
            }
            List<BizArticle> listTag = bizArticleMapper.listTagsByArticleId(ids);
            // listTag, 重新组装数据为{id: Article}
            Map<Integer, BizArticle> tagMap = new LinkedHashMap<>(listTag.size());
            for (BizArticle bizArticle : listTag) {
                tagMap.put(bizArticle.getId(), bizArticle);
            }

            for (BizArticle bizArticle : list) {
                BizArticle tagArticle = tagMap.get(bizArticle.getId());
                if (Objects.nonNull(tagArticle)) {
                    bizArticle.setTags(tagArticle.getTags());
                }
            }
        }
        return list;
    }

    @Override
    @Cacheable(value = "article", key = "'slider'")
    public List<BizArticle> sliderList() {
        ArticleConditionVo vo = new ArticleConditionVo();
        vo.setSlider(true);
        vo.setStatus(CoreConst.STATUS_VALID);
        return this.findByCondition(null, vo);
    }

    @Override
    @Cacheable(value = "article", key = "'recommended'")
    public List<BizArticle> recommendedList(int pageSize) {
        ArticleConditionVo vo = new ArticleConditionVo();
        vo.setRecommended(true);
        vo.setStatus(CoreConst.STATUS_VALID);
        vo.setPageSize(pageSize);
        IPage<BizArticle> page = new Pagination<>(vo.getPageNumber(), vo.getPageSize());
        return this.findByCondition(page, vo);
    }

    @Override
    @Cacheable(value = "article", key = "'recent'")
    public List<BizArticle> recentList(int pageSize) {
        ArticleConditionVo vo = new ArticleConditionVo();
        vo.setPageSize(pageSize);
        vo.setStatus(CoreConst.STATUS_VALID);
        vo.setRecentFlag(true);
        IPage<BizArticle> page = new Pagination<>(vo.getPageNumber(), vo.getPageSize());
        return this.findByCondition(page, vo);
    }

    @Override
    @Cacheable(value = "article", key = "'random'")
    public List<BizArticle> randomList(int pageSize) {
        ArticleConditionVo vo = new ArticleConditionVo();
        vo.setRandom(true);
        vo.setStatus(CoreConst.STATUS_VALID);
        vo.setPageSize(pageSize);
        IPage<BizArticle> page = new Pagination<>(vo.getPageNumber(), vo.getPageSize());
        return this.findByCondition(page, vo);
    }

    @Override
    @Cacheable(value = "article", key = "'hot'")
    public List<BizArticle> hotList(int pageSize) {
        IPage<BizArticle> page = new Pagination<>(1, pageSize);
        return bizArticleMapper.hotList(page);
    }

    @Override
    @Cacheable(value = "article", key = "#id")
    public BizArticle selectById(Integer id) {
        return bizArticleMapper.getById(id);
    }

    @Override
    @CacheEvict(value = "article", allEntries = true)
    public BizArticle insertArticle(BizArticle bizArticle) {
        Date date = new Date();
        bizArticle.setCreateTime(date);
        bizArticle.setUpdateTime(date);
        bizArticleMapper.insert(bizArticle);
        return bizArticle;
    }

    @Override
    @CacheEvict(value = "article", allEntries = true)
    public int deleteBatch(Integer[] ids) {
        return bizArticleMapper.deleteBatch(ids);
    }

    @Override
    public List<BizArticle> selectByCategoryId(Integer categoryId) {
        return bizArticleMapper.selectList(Wrappers.<BizArticle>lambdaQuery().eq(BizArticle::getCategoryId, categoryId));
    }

}
