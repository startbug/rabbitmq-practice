package com.starbug.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *  @Author Starbug
 *  @Date 2021/6/18 13:22
 */
public class RabbitMQUtils {

    //得到一个Channel
    public static Channel getChannel() {
        //创建一个连接工厂
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("192.168.232.145");
            factory.setUsername("admin");
            factory.setPassword("123456");
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return channel;
    }

}
