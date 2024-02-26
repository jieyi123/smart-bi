package com.pjieyi.smartbi.mq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

//单机生产者 用于实现消息发送功能
public class TTLProducer {
    private final static String QUEUE_NAME = "ttl_queue";

    public static void main(String[] argv) throws Exception {
        // 创建一个ConnectionFactory对象，这个对象可以用于创建到RabbitMQ服务器的连接
        ConnectionFactory factory = new ConnectionFactory();
        // 设置ConnectionFactory的主机名为"localhost"，这表示我们将连接到本地运行的RabbitMQ服务器
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             // 通过已建立的连接创建一个新的频道
             Channel channel = connection.createChannel()) {
            // 消息虽然可以重复声明,必须指定相同的参数,在消费者的创建队列要指定过期时间,
            // 后面要放args,在生产者你又想重新创建队列，又不指定参数，那肯定会有问题，
            // 所以要把这里的创建队列注释掉。
            // channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello World!";

            //给消息指定过期时间
            AMQP.BasicProperties properties=new AMQP.BasicProperties.Builder()
                    .expiration("5000")
                            .build();
            // 使用channel.basicPublish方法将消息发布到指定的队列中。这里我们指定的队列名为"hello"
            channel.basicPublish("", QUEUE_NAME, properties, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}