package com.starbug.rabbitmq.die;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.starbug.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 *  @Author Starbug
 *  @Date 2021/6/20 12:47
 */
public class Producer {

    public static final String EXCHANGE_NAME = "normal_exchange";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        //死信消息,需要设置TTL时间(time to live),存活时间,单位:ms
//        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
        for (int i = 0; i < 11; i++) {
            String message = "info" + i;
            channel.basicPublish(EXCHANGE_NAME, "zhangsan", null, message.getBytes(StandardCharsets.UTF_8));
        }

    }

}
