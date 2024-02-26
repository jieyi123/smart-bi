package com.pjieyi.smartbi.bizmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.pjieyi.smartbi.constant.BiMqConstant.BI_EXCHANGE_NAME;
import static com.pjieyi.smartbi.constant.BiMqConstant.BI_ROUTING_KEY;

/**
 * @author pjieyi
 * @desc 消息队列发送信息生产者
 */
@Component
public class BIMessageProducer {

    //对rabbitTemplate进行依赖注入
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * @param message 消息内容，要发送的具体消息
     */
    public void sendMessage(String message){
        // 使用rabbitTemplate的convertAndSend方法将消息发送到指定的交换机和路由键
        rabbitTemplate.convertAndSend(BI_EXCHANGE_NAME,BI_ROUTING_KEY,message);
    }
}
