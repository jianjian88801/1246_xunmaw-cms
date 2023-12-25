package com.xunmaw.cms.module.admin.vo;

import lombok.Data;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Data
public class ChangePasswordVo {

    String oldPassword;
    String newPassword;
    String confirmNewPassword;

}
