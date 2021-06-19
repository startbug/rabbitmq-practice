package com.starbug.rabbitmq.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.starbug.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 *  @Author Starbug
 *  @Date 2021/6/19 23:00
 */
public class EmitLog {
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        Scanner sc = new Scanner(System.in);

        while (sc.hasNext()) {
            String next = sc.next();
            channel.basicPublish(EXCHANGE_NAME, "", null, next.getBytes(StandardCharsets.UTF_8));
            System.out.println("发出的消息:" + next);
        }

    }
}
