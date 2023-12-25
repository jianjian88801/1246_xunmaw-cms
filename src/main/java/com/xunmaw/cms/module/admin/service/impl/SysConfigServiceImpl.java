package com.xunmaw.cms.module.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunmaw.cms.common.util.CoreConst;
import com.xunmaw.cms.module.admin.mapper.SysConfigMapper;
import com.xunmaw.cms.module.admin.model.SysConfig;
import com.xunmaw.cms.module.admin.service.SysConfigService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Service
@AllArgsConstructor
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    private final SysConfigMapper sysConfigMapper;

    @PostConstruct
    public void init() {
        CoreConst.SITE_STATIC.set("on".equalsIgnoreCase(selectAll().getOrDefault(CoreConst.SITE_STATIC_KEY, "false")));
    }

    @Override
    @Cacheable(value = "site", key = "'config'")
    public Map<String, String> selectAll() {
        List<SysConfig> sysConfigs = sysConfigMapper.selectList(Wrappers.emptyWrapper());
        Map<String, String> map = new HashMap<>(sysConfigs.size());
        for (SysConfig config : sysConfigs) {
            map.put(config.getSysKey(), config.getSysValue());
        }
        return map;
    }

    @Override
    @CacheEvict(value = "site", key = "'config'", allEntries = true)
    public boolean updateByKey(String key, String value) {
        if (getOne(Wrappers.<SysConfig>lambdaQuery().eq(SysConfig::getSysKey, key)) != null) {
            return update(Wrappers.<SysConfig>lambdaUpdate().eq(SysConfig::getSysKey, key).set(SysConfig::getSysValue, value));
        } else {
            return save(new SysConfig().setSysKey(key).setSysValue(value));
        }
    }

    @Override
    @CacheEvict(value = "site", key = "'config'", allEntries = true)
    public void updateAll(Map<String, String> map, HttpServletRequest request, HttpServletResponse response) {
        map.forEach(this::updateByKey);
    }
}
