package com.starbug.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *  @Author Starbug
 *  @Date 2021/6/17 22:51
 */
public class Consumer {

    //队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.232.145");
        factory.setUsername("admin");
        factory.setPassword("123456");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费被中断");
        };

        /**
         * 消费消息
         * 1.消费的那个队列
         * 2.消费成功之后是否要自动应答,true表示自动应答,false表示手动应答
         * 3.消费者成功消费的回调
         * 4.消费者取消消费的回调
         **/
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }

}
