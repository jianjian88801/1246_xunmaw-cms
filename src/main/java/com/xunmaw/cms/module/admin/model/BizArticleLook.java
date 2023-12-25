package com.xunmaw.cms.module.admin.model;

import com.xunmaw.cms.module.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizArticleLook extends BaseVo {
    private static final long serialVersionUID = 1052723347580827581L;

    private Integer articleId;
    private String userId;
    private String userIp;
    private Date lookTime;

}
