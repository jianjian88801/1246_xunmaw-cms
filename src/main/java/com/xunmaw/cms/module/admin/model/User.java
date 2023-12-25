package com.xunmaw.cms.module.admin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = -8736616045315083846L;


    /**
     * 用户id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String userId;

    /**
     * 用户名
     */
    private String username;

    private String password;

    /**
     * 加密盐值
     */
    private String salt;

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 年龄：1男2女
     */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 头像
     */
    private String img;

    /**
     * 用户状态：1有效; 0无效
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 登录ip
     */
    @TableField(exist = false)
    private String loginIpAddress;

    /**
     * 角色
     */
    @TableField(exist = false)
    private List<Role> roles;

    /**
     * 重写获取盐值方法，自定义realm使用
     * Gets credentials salt.
     *
     * @return the credentials salt
     */
    public String getCredentialsSalt() {
        return username + "puboot.com" + salt;
    }


}
