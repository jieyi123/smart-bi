package com.pjieyi.smartbi.mq;

import com.rabbitmq.client.*;

import java.util.HashMap;
import java.util.Map;

//死信队列
public class DlxDirectConsumer {

    private static final String DEAD_EXCHANGE_NAME = "dlx-direct-exchange";
    private static final String WORk_EXCHANGE_NAME = "work-exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        // 创建通道
        Channel channel1 = connection.createChannel();
        // 声明交换机
        channel1.exchangeDeclare(WORk_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 创建队列，随机分配一个队列名称
        String queueName = "xiaodog_queue";
        //指定死信队列
        Map<String, Object> args = new HashMap<>();
        // 要绑定到哪个交换机
        args.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        // 指定死信要转发到哪个死信队列
        args.put("x-dead-letter-routing-key", "laoban");
        args.put("x-message-ttl", 5000);
        channel1.queueDeclare(queueName, true, false, false, args);
        //绑定交换机
        channel1.queueBind(queueName, WORk_EXCHANGE_NAME, "xiaodog");

        // 创建队列2
        String queueName2 = "xiaocat_queue";
        Map<String, Object> args2 = new HashMap<>();
        // 要绑定到哪个交换机
        args2.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        // 指定死信要转发到哪个死信队列
        args2.put("x-dead-letter-routing-key", "waibao");
        channel1.queueDeclare(queueName2, true, false, false,args2);
        channel1.queueBind(queueName2, WORk_EXCHANGE_NAME, "xiaocat");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        // 创建交付回调函数1
        DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            //拒绝消息
            //channel1.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
            //确认消息
           // channel1.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            System.out.println(" [xiaodog] Received '" + delivery.getEnvelope().getRoutingKey() + ":" + message + "'");
        };
        // 创建交付回调函数2
        DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            //拒绝消息
            channel1.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
            System.out.println(" [xiaocat] Received '" + delivery.getEnvelope().getRoutingKey() + ":" + message + "'");
        };
        // 开始消费消息队列1
        channel1.basicConsume(queueName, false, deliverCallback1, consumerTag -> {
        });
        // 开始消费消息队列2
        channel1.basicConsume(queueName2, false, deliverCallback2, consumerTag -> {
        });


    }
}