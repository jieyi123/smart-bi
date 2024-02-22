package com.pjieyi.smartbi.model.dto.chart;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * AI智能分析
 * @author pjieyi
 */
@Data
public class GenChartByAiRequest implements Serializable {


    /**
     * 图表名称
     */
    private String name;

    /**
     * 分析目标
     */
    private String goal;


    /**
     * 图表类型
     */
    private String chartType;



    private static final long serialVersionUID = 1L;
}