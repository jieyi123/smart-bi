package com.pjieyi.smartbi.model.vo;

import lombok.Data;

/**
 * @author pjieyi
 * @desc AI响应结果
 */
@Data
public class BiResponse {
    /**
     * 图表数据
     */
    private String genChart;
    /**
     * 响应结果
     */
    private String genResult;
    /**
     * 图表id
     */
    private Long chartId;
}
