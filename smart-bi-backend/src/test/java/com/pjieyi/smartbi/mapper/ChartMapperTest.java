package com.pjieyi.smartbi.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author pjieyi
 * @desc
 */
@SpringBootTest
class ChartMapperTest {

    @Resource
    private ChartMapper chartMapper;
    @Test
    void queryChartData() {
        Long id=1760549415340978177L;
        String sql="select * from chart_"+id;
        List<Map<String, Object>> maps = chartMapper.queryChartData(sql);
        System.out.println(maps);

    }
}