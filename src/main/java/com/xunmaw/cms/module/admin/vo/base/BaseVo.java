package com.xunmaw.cms.module.admin.vo.base;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Data
public abstract class BaseVo implements Serializable {
    private static final long serialVersionUID = 1L;


    private Integer id;

    private Date createTime;
    private Date updateTime;

}