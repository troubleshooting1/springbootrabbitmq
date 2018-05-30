package com.anji.springbootrabbitmqhelloworld.msg3;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Date;

/**
 * Description:
 * author: chenqiang
 * date: 2018/5/29 13:36
 */
public class Worker {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        channel.basicQos(1);

        final Consumer consumer = new DefaultConsumer(channel) {
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                System.out.println(" [x] Received '" + message + "'");
                System.out.println(" [x] Proccessing... at " + new Date().toString());
                try {
                    for (char ch : message.toCharArray()) {
                        if (ch == '.') {
                            Thread.sleep(1000);
                        }
                    }
                } catch (InterruptedException e) {

                } finally {
                    System.out.println(" [x] Done! at " + new Date().toString());
                }
            }
        };

//        channel.basicConsume(QUEUE_NAME, true, consumer);
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}
