package com.xunmaw.cms.module.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xunmaw.cms.module.admin.model.BizLove;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
public interface BizLoveService extends IService<BizLove> {

    BizLove checkLove(Integer bizId, String userIp);
}
