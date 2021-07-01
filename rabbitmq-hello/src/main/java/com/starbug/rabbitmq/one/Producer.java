package com.starbug.rabbitmq.one;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @Author Starbug
 * @Date 2021/6/17 22:27
 */
public class Producer {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂IP连接RabbitMQ的队列
        factory.setHost("192.168.232.145");
        factory.setUsername("admin");
        factory.setPassword("123456");
        //创建连接
        Connection connection = factory.newConnection();
        //根据连接创建信道
        Channel channel = connection.createChannel();
        //简单操作,可以使用默认交换机,不需要手动指定交换机
        /**
         * 声明一个队列
         * 1.队列名称
         * 2.是否持久化
         * 3.该队列是否只提供一个消费者进行消费,是否进行消息共享,true可以多个消费者消费,false只能一个消费者消费
         * 4.是否自动删除,最后一个消费者断开连接后,该队列是否自动删除,true自动删除,false亦然
         **/
        //为队列设置优先级范围0-10(官方允许范围为0-255),不适宜设置过大,浪费CPU和内存
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority", 10);
        channel.queueDeclare(QUEUE_NAME, false, false, false, arguments);
        //发消息
        String message = "hello world";
        /**
         * 发送一个消息
         * 1.发送到哪一个交换机,空字符串表示默认交换机
         * 2.路由的key值是什么,本次时队列的名称
         * 3.其他参数信息
         * 4.发送的消息
         **/
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                //info5这条消息优先被消费(优先级为5)
                channel.basicPublish("", QUEUE_NAME, properties, ("info" + i).getBytes(StandardCharsets.UTF_8));
            } else {
                channel.basicPublish("", QUEUE_NAME, null, ("info" + i).getBytes(StandardCharsets.UTF_8));
            }
        }
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
        System.out.println("发送完成");
    }

}
