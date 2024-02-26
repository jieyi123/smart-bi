package com.pjieyi.smartbi.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

//Topics
public class TopicProducer {

  private static final String EXCHANGE_NAME = "topic_exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    try (Connection connection = factory.newConnection();
         Channel channel = connection.createChannel()) {
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
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
            channel.basicPublish(EXCHANGE_NAME, routeingKey,
                    null,
                    message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
  }
  //..
}