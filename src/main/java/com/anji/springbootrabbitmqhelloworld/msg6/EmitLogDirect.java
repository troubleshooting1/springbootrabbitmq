package com.anji.springbootrabbitmqhelloworld.msg6;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description:
 * author: chenqiang
 * date: 2018/5/29 14:52
 */
public class EmitLogDirect {
    public final static String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //指定转发—广播
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        //所有日志严重性级别
        String[] facilities = {"auth", "cron", "kern", "auth.A"};
        String[] severities = {"error", "info", "warning"};

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                String routingKey = facilities[i] + "." + severities[j % 3];

                //发送消息
                String message = " Hello World!";

                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
                System.out.println(" [x] Sent [" + routingKey + "] : '" + message + "'");
            }
        }

        //        for (int i = 0; i < 3; i++) {
        //            String severity = severities[i % 3];    //每一次发一次不同严重程度的日志
        //
        //            String message = "Hello World";
        //            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
        //            System.out.println(" [x] Sent '" + severity + "':'" + message + "'");
        //        }

        channel.close();
        connection.close();
    }
}
