package com.starbug.rabbitmq.die;

import com.rabbitmq.client.Channel;
import com.starbug.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;

/**
 *  @Author Starbug
 *  @Date 2021/6/20 12:10
 *  死信队列
 *
 */
public class Consumer2 {

    public static final String DEAD_QUEUE_NAME = "dead_queue";

    public static void main(String[] args) throws IOException {

        Channel channel = RabbitMQUtils.getChannel();

        channel.basicConsume(DEAD_QUEUE_NAME, true, (consumerTag, message) -> {
            System.out.println("C2介绍消息:" + new String(message.getBody()));
        }, consumerTag -> {
        });

    }

}
