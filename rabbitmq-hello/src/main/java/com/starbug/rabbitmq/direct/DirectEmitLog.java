package com.starbug.rabbitmq.direct;

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
public class DirectEmitLog {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        Scanner sc = new Scanner(System.in);

        while (sc.hasNext()) {
            String next = sc.next();
            channel.basicPublish(EXCHANGE_NAME, "error", null, next.getBytes(StandardCharsets.UTF_8));
            System.out.println("发出的消息:" + next);
        }

    }
}
