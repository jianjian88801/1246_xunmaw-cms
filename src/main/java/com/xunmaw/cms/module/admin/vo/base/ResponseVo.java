package com.xunmaw.cms.module.admin.vo.base;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Data
@AllArgsConstructor
public class ResponseVo<T> {

    private Integer status;
    private String msg;
    private T data;

    public ResponseVo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

}
