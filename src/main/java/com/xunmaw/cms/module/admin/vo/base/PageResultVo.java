package com.xunmaw.cms.module.admin.vo.base;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Linzhaoguan
 * @version V1.0
 * @date 2019年9月11日
 */
@Data
@AllArgsConstructor
public class PageResultVo {
    private List rows;
    private Long total;

}
