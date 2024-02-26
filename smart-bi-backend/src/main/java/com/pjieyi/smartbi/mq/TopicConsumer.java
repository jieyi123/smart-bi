package com.pjieyi.smartbi.mq;

import com.rabbitmq.client.*;

//Topics
public class TopicConsumer {

  private static final String EXCHANGE_NAME = "topic_exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
      // 创建两个通道
      Channel channel1 = connection.createChannel();
      // 声明交换机
      channel1.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
      // 创建队列1，随机分配一个队列名称
      String queueName = "frontend_queue";
      channel1.queueDeclare(queueName, true, false, false, null);
      //绑定交换机
      //#-0个或多个字符   *-一个字符
      channel1.queueBind(queueName, EXCHANGE_NAME, "#.前端.#");

      // 创建队列2
      String queueName2 = "backend_queue";
      channel1.queueDeclare(queueName2, true, false, false, null);
      channel1.queueBind(queueName2, EXCHANGE_NAME, "#.后端.#");
      // 创建队列3
      String queueName3 = "product_queue";
      channel1.queueDeclare(queueName3, true, false, false, null);
      channel1.queueBind(queueName3, EXCHANGE_NAME, "#.产品.#");

      System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
      // 创建交付回调函数1
      DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), "UTF-8");
          System.out.println(" [前端] Received '" + message + "'");
      };
      // 创建交付回调函数2
      DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), "UTF-8");
          System.out.println(" [后端] Received '" + message + "'");
      };
      // 创建交付回调函数3
      DeliverCallback deliverCallback3 = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), "UTF-8");
          System.out.println(" [产品] Received '" + message + "'");
      };
      // 开始消费消息队列1
      channel1.basicConsume(queueName, true, deliverCallback1, consumerTag -> { });
      // 开始消费消息队列2
      channel1.basicConsume(queueName2, true, deliverCallback2, consumerTag -> { });
      // 开始消费消息队列3
      channel1.basicConsume(queueName3, true, deliverCallback2, consumerTag -> { });
  }
}