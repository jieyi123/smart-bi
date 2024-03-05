package com.pjieyi.smartbi.mapper;

import com.pjieyi.smartbi.model.entity.Chart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
* @author pjy17
* @description 针对表【chart(图表信息表)】的数据库操作Mapper
* @createDate 2024-02-21 18:38:15
* @Entity com.pjieyi.smartbi.model.entity.Chart
*/
public interface ChartMapper extends BaseMapper<Chart> {

    List<Map<String,Object>> queryChartData(String querySql);

    void createDataTable(String id,String[] headList);
    int insertDataTable(String chartId,List<String> dataList);

    void dropTableIfExists(String chartId);

}




