package com.pjieyi.smartbi.mq;

import com.rabbitmq.client.*;

import java.util.Scanner;

//发布订阅模式
public class FanoutProducer {

    private static final String EXCHANGE_NAME = "fanout-exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // 声明交换机
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            Scanner scanner=new Scanner(System.in);
            while (scanner.hasNext()){
                //String message = String.join(" ", argv);
                // 读取用户在控制台输入的下一行文本
                String message = scanner.nextLine();
                // 将消息发送到指定的交换机（fanout交换机），不指定路由键（空字符串）
                channel.basicPublish(EXCHANGE_NAME, "",
                        null,
                        message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");
            }
        }
    }

}