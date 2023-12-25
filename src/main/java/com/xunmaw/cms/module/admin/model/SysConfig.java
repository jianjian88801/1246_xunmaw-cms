package com.xunmaw.cms.module.admin.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Data
@Accessors(chain = true)
public class SysConfig implements Serializable {

    private static final long serialVersionUID = -1645880315099183738L;

    private Integer id;

    /**
     * key
     */
    private String sysKey;

    /**
     * value
     */
    private String sysValue;

    /**
     * 状态  1：有效 0：无效
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

}