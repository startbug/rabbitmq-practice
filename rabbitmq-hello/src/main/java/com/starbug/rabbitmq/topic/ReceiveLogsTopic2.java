package com.starbug.rabbitmq.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.starbug.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;

/**
 *  @Author Starbug
 *  @Date 2021/6/19 23:22
 *  Topic主题模式
 */
public class ReceiveLogsTopic2 {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        String queueName = "Q2";
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind(queueName, EXCHANGE_NAME, "lazy.#");
        System.out.println("等待接收消息");

        channel.basicConsume(queueName, true, (consumerTag, message) -> {
            System.out.println("22222消息:" + new String(message.getBody()));
            System.out.println("接收队列" + queueName + " 绑定键:" + message.getEnvelope().getRoutingKey());
        }, consumerTag -> {
            System.out.println("取消....");
        });
    }


}
