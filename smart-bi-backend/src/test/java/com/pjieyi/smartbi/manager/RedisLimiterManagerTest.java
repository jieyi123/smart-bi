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
class RedisLimiterManagerTest {

    @Resource
    private RedisLimiterManager redisLimiterManager;
    @Test
    void doRateLimit() {
        String userId="1";
        for (int i=0;i<2;i++){
            redisLimiterManager.doRateLimit(userId);
            System.out.println("请求成功");
        }
        try {
            System.out.println("=====");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int i=0;i<5;i++){
            redisLimiterManager.doRateLimit(userId);
            System.out.println("请求成功");
        }
    }
}