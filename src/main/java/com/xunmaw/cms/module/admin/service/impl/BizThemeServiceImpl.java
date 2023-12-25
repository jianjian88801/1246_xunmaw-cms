package com.xunmaw.cms.module.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunmaw.cms.common.util.CoreConst;
import com.xunmaw.cms.module.admin.mapper.BizThemeMapper;
import com.xunmaw.cms.module.admin.model.BizTheme;
import com.xunmaw.cms.module.admin.service.BizThemeService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Service
@AllArgsConstructor
public class BizThemeServiceImpl extends ServiceImpl<BizThemeMapper, BizTheme> implements BizThemeService {

    private final BizThemeMapper themeMapper;

    @Override
    @CacheEvict(value = "theme", allEntries = true)
    public int useTheme(Integer id) {
        themeMapper.setInvaid();
        return themeMapper.updateStatusById(id);
    }

    @Override
    @Cacheable(value = "theme", key = "'current'")
    public BizTheme selectCurrent() {
        return themeMapper.selectOne(Wrappers.<BizTheme>lambdaQuery().eq(BizTheme::getStatus, CoreConst.STATUS_VALID));
    }

    @Override
    @CacheEvict(value = "theme", allEntries = true)
    public int deleteBatch(Integer[] ids) {
        return themeMapper.deleteBatch(ids);
    }
}
