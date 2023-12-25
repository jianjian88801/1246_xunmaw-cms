package com.xunmaw.cms.module.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xunmaw.cms.module.admin.model.BizTheme;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
public interface BizThemeService extends IService<BizTheme> {

    int useTheme(Integer id);

    BizTheme selectCurrent();

    int deleteBatch(Integer[] ids);

}
