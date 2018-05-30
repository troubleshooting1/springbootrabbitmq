package com.anji.springbootrabbitmqhelloworld.msg2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Description:
 * author: chenqiang
 * date: 2018/5/29 10:55
 */
public class NewTask {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //        String message="Hello World...";
        //        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        String point = ".";
        for (int i = 0; i < 5; i++) {

            // 发送的消息
            String message = "Hello World" + point;
            // 往队列中发出一条消息
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");

            point += ".";
        }

        //        System.out.println(" [x] Sent '" + message + "'");
        channel.close();
        connection.close();
    }
}
