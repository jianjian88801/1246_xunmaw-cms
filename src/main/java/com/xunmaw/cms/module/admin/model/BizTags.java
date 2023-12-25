package com.xunmaw.cms.module.admin.model;

import com.xunmaw.cms.module.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizTags extends BaseVo {
    private static final long serialVersionUID = 3578477956306175100L;

    private String name;
    private String description;

}
