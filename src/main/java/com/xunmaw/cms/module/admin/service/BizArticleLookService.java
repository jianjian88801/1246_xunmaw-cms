package com.xunmaw.cms.module.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xunmaw.cms.module.admin.model.BizArticleLook;

import java.util.Date;
import java.util.Map;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
public interface BizArticleLookService extends IService<BizArticleLook> {

    int checkArticleLook(Integer articleId, String userIp, Date lookTime);

    Map<String, Integer> lookCountByDay(int day);

    Map<String, Integer> userCountByDay(int day);
}
