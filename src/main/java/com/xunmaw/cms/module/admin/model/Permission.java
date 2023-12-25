package com.xunmaw.cms.module.admin.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Data
@Accessors(chain = true)
public class Permission implements Serializable {

    private static final long serialVersionUID = -4317225965160245362L;

    private Integer id;

    /**
     * 权限id
     */
    private String permissionId;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 权限访问路径
     */
    private String url;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 父级权限id
     */
    private Integer parentId;

    /**
     * 类型   0：目录   1：菜单   2：按钮
     */
    private Integer type;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 图标
     */
    private String icon;
    /**
     * 状态：1有效; 0无效
     */
    private Integer status;

    private Date createTime;

    private Date updateTime;

    @TableField(exist = false)
    private List<Permission> children;

}