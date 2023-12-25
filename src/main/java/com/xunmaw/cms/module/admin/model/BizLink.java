package com.xunmaw.cms.module.admin.model;

import com.xunmaw.cms.module.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class BizLink extends BaseVo {
    private static final long serialVersionUID = -6511423333796987519L;

    private String url;
    private String name;
    private String description;
    private String img;
    private String email;
    private String qq;
    private Integer status;
    private Integer origin;
    private String remark;

}
