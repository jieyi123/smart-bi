package com.pjieyi.smartbi.mq;

import com.rabbitmq.client.*;

import java.util.Scanner;

/**
 * @author pjieyi
 * @desc 死信队列
 */
public class DlxDirectProducer {
    private static final String DEAD_EXCHANGE_NAME = "dlx-direct-exchange";
    private static final String WORk_EXCHANGE_NAME = "work-exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            //声明死信交换机
            channel.exchangeDeclare(DEAD_EXCHANGE_NAME,"direct");
            channel.exchangeDeclare(DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            // 创建队列1，随机分配一个队列名称
            String queueName = "laoban_dlx_queue";
            channel.queueDeclare(queueName, true, false, false, null);
            //绑定交换机
            channel.queueBind(queueName, DEAD_EXCHANGE_NAME, "laoban");

            // 创建队列2
            String queueName2 = "waibao_dlx_queue";
            channel.queueDeclare(queueName2, true, false, false, null);
            channel.queueBind(queueName2, DEAD_EXCHANGE_NAME, "waibao");

            // 创建交付回调函数1
            DeliverCallback laobanDeliverCallback1 = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                //拒绝消息
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
                System.out.println(" [laoban] Received '" +delivery.getEnvelope().getRoutingKey()+":"+ message + "'");
            };
            // 创建交付回调函数2
            DeliverCallback waibaoDeliverCallback2 = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                //拒绝消息
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,false);
                System.out.println(" [waibao] Received '"  +delivery.getEnvelope().getRoutingKey()+":"+ message + "'");
            };
            // 开始消费死信队列1
            channel.basicConsume(queueName, false, laobanDeliverCallback1, consumerTag -> { });
            // 开始消费死信队列2
            channel.basicConsume(queueName2, false, waibaoDeliverCallback2, consumerTag -> { });


            channel.exchangeDeclare(WORk_EXCHANGE_NAME, "direct");
            Scanner scanner=new Scanner(System.in);
            while (scanner.hasNext()) {
                String userInput = scanner.nextLine();
                //根据空格进行动态的发送 前面是发送的信息 后面是routingKey
                String[] splits = userInput.split(" ");
                if (splits.length < 1) {
                    System.out.println("你输入的格式不对");
                    continue;
                }
                String message = splits[0];
                String routeingKey = splits[1];
                channel.basicPublish(WORk_EXCHANGE_NAME, routeingKey,
                        null,
                        message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");
            }
        }
    }
}
