package com.pjieyi.smartbi.bizmq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author pjieyi
 * @desc
 */
@SpringBootTest
class MyMessageProducerTest {

    @Resource
    private MyMessageProducer producer;
    @Test
    void sendMessage() {
        // 调用消息生产者的 sendMessage 方法发送消息
        producer.sendMessage("code_exchange","my_routingKey","你好");
    }
}