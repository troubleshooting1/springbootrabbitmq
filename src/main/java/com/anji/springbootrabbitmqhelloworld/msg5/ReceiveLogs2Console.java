package com.anji.springbootrabbitmqhelloworld.msg5;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description:
 * author: chenqiang
 * date: 2018/5/29 14:35
 */
public class ReceiveLogs2Console {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        //申明一个随机队列
        String queueName = channel.queueDeclare().getQueue();

        //所有日志严重性级别
        String[] severities = {"error", "info", "warning"};
        for (String severity : severities) {
            //关注所有级别的日志（多重绑定）
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
        }

        //        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");

            }
        };

        channel.basicConsume(queueName, true, consumer);

    }
}
