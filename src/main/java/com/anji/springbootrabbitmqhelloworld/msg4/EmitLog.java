package com.anji.springbootrabbitmqhelloworld.msg4;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description:
 * author: chenqiang
 * date: 2018/5/29 14:05
 */
public class EmitLog {
    public final static String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("127.0.0.1");

        Connection connection=factory.newConnection();
        Channel channel=connection.createChannel();
        //指定转发—广播
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        for (int i=0;i<3;i++){
            String message="Hello World";
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }

        channel.close();
        connection.close();
    }
}
