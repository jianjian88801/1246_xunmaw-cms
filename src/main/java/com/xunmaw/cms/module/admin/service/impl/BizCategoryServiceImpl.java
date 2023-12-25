package com.xunmaw.cms.module.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunmaw.cms.module.admin.mapper.BizCategoryMapper;
import com.xunmaw.cms.module.admin.model.BizCategory;
import com.xunmaw.cms.module.admin.service.BizCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Service
@AllArgsConstructor
public class BizCategoryServiceImpl extends ServiceImpl<BizCategoryMapper, BizCategory> implements BizCategoryService {

    private final BizCategoryMapper bizCategoryMapper;

    @Override
    @Cacheable(value = "category", key = "'tree'")
    public List<BizCategory> selectCategories(BizCategory bizCategory) {
        return bizCategoryMapper.selectCategories(bizCategory);
    }

    @Override
    @CacheEvict(value = "category", allEntries = true)
    public int deleteBatch(Integer[] ids) {
        return bizCategoryMapper.deleteBatch(ids);
    }

    @Override
    public BizCategory selectById(Integer id) {
        return bizCategoryMapper.getById(id);
    }

    @Override
    public List<BizCategory> selectByPid(Integer pid) {
        return bizCategoryMapper.selectList(Wrappers.<BizCategory>lambdaQuery().eq(BizCategory::getPid, pid));
    }
}
