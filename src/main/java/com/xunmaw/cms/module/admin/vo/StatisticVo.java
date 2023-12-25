package com.xunmaw.cms.module.admin.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Nobita
 * @date 2021/1/12 3:35 下午
 */
@Data
@Builder
public class StatisticVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long articleCount;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long commentCount;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long lookCount;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userCount;
    private Map<String, Integer> lookCountByDay;
    private Map<String, Integer> userCountByDay;

}
