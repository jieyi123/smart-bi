package com.pjieyi.smartbi.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author pjieyi
 * @desc
 */
@SpringBootTest
class AiManagerTest {

    @Resource
    private AiManager aiManager;
    @Test
    void doChartBi() {
        String message="分析需求：\n" +
                "分析网站用户的增长情况\n" +
                "原始数据：\n" +
                "日期,用户数\n" +
                "1号,10\n" +
                "2号,20\n" +
                "3号,30\n" +
                "4号,15\n" +
                "5号,14\n" +
                "6号,20";
        Long modelId=1756296792985034754L;
        String res = aiManager.doChart(message, modelId);
        System.out.println(res);
    }
    @Test
    void doChart(){
        String message="你能干什么";
        //通用模型
        Long modelId=1654785040361893889L;
        String res = aiManager.doChart(message, modelId);
        System.out.println(res);
    }
}