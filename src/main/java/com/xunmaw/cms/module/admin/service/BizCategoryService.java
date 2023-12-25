package com.xunmaw.cms.module.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xunmaw.cms.module.admin.model.BizCategory;

import java.util.List;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
public interface BizCategoryService extends IService<BizCategory> {

    List<BizCategory> selectCategories(BizCategory bizCategory);

    int deleteBatch(Integer[] ids);

    BizCategory selectById(Integer id);

    List<BizCategory> selectByPid(Integer pid);

}
