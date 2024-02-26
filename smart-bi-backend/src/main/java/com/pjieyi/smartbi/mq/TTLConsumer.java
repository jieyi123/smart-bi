package com.pjieyi.smartbi.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

//单机消费者
public class TTLConsumer {

  private final static String QUEUE_NAME = "ttl_queue";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    // 从连接中创建一个新的频道
    Connection connection = factory.newConnection();
    // 从连接中创建一个新的频道
    Channel channel = connection.createChannel();
    // 创建队列，指定消息过期参数
    Map<String, Object> args = new HashMap<String, Object>();
    //5秒未被消费就会过期
    //如果消息已经接收到，但是没确认，是不会过期的
    args.put("x-message-ttl", 5000);
    // 创建队列,在该频道上声明我们正在监听的队列
    channel.queueDeclare(QUEUE_NAME, false, false, false,null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    // 定义了如何处理消息,创建一个新的DeliverCallback来处理接收到的消息
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      // 将消息体转换为字符串
      String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
      System.out.println(" [x] Received '" + message + "'");
    };
    //手动确认消息
    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
  }
}
