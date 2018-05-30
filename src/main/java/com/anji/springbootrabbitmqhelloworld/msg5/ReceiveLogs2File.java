package com.anji.springbootrabbitmqhelloworld.msg5;

import com.rabbitmq.client.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * Description:
 * author: chenqiang
 * date: 2018/5/29 14:37
 */
public class ReceiveLogs2File {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        //申明一个随机队列
        String queueName = channel.queueDeclare().getQueue();

        String severity = "error";

        channel.queueBind(queueName, EXCHANGE_NAME, severity);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                print2File( "["+ envelope.getRoutingKey() + "] "+message);
                //                print2File(message);
                //                System.out.println(" [x] Received '" + message + "'");
            }
        };

        channel.basicConsume(queueName, true, consumer);
    }

    private static void print2File(String msg) {
        try {
            String dir = com.anji.springbootrabbitmqhelloworld.msg4.ReceiveLogs2File.class.getClassLoader().getResource("").getPath();
            String logFileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            File file = new File(dir, logFileName + ".log");
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write((new SimpleDateFormat("HH:mm:ss").format(new Date()) + " - " + msg + "\r\n").getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
