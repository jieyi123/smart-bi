package com.pjieyi.smartbi.mq;

import com.rabbitmq.client.*;

//Routing
public class DirectConsumer {

  private static final String EXCHANGE_NAME = "direct_exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
      Connection connection = factory.newConnection();
      // 创建通道
      Channel channel1 = connection.createChannel();
      // 声明交换机
      channel1.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
      channel1.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
      // 创建队列1，随机分配一个队列名称
      String queueName = "ls_queue";
      channel1.queueDeclare(queueName, true, false, false, null);
      //绑定交换机
      channel1.queueBind(queueName, EXCHANGE_NAME, "ls");

      // 创建队列2
      String queueName2 = "zs_queue";
      channel1.queueDeclare(queueName2, true, false, false, null);
      channel1.queueBind(queueName2, EXCHANGE_NAME, "zs");

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
      // 创建交付回调函数1
      DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), "UTF-8");
          System.out.println(" [ls] Received '" + message + "'");
      };
      // 创建交付回调函数2
      DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), "UTF-8");
          System.out.println(" [zs] Received '" + message + "'");
      };
      // 开始消费消息队列1
      channel1.basicConsume(queueName, true, deliverCallback1, consumerTag -> { });
      // 开始消费消息队列2
      channel1.basicConsume(queueName2, true, deliverCallback2, consumerTag -> { });



  }
}