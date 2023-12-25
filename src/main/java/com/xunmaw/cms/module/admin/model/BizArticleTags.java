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
public class BizArticleTags extends BaseVo {
    private static final long serialVersionUID = 2627147974506469978L;

    private Integer tagId;
    private Integer articleId;

}
