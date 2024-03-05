package com.pjieyi.smartbi.service.impl;

import com.pjieyi.smartbi.mapper.ChartMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author pjieyi
 * @desc
 */
@SpringBootTest
class ChartServiceImplTest {

    @Resource
    private ChartServiceImpl chartService;
    @Test
    void createTable() {
        String data="日期,用户数\n" +
                "2023/2/21,10\n" +
                "2023/2/22,20\n" +
                "2023/2/23,30\n" +
                "2023/2/24,50\n" +
                "2023/2/25,32\n" +
                "2023/2/26,25\n" +
                "2023/2/27,20\n" +
                "2023/2/28,40\n" +
                "2023/3/1,45";

    }
}