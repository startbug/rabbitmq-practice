package com.starbug.rabbitmq.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.starbug.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;

/**
 *  @Author Starbug
 *  @Date 2021/6/19 23:08
 *  direct模式,就是订阅发布模式(fanout)基础上,给每个队列指定一个独特的routing key,让交换机转发给指定的队列
 */
public class ReceiveLogsDirect2 {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException {

        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare("disk", false, false, false, null);

        channel.queueBind("disk", EXCHANGE_NAME, "error");

        channel.basicConsume("disk", true, (consumerTag, message) -> {
            System.out.println("logs2接收到的消息:" + new String(message.getBody()));
        }, consumerTag -> {
        });

    }


}
