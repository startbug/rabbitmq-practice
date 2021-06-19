package com.starbug.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.starbug.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 *  @Author Starbug
 *  @Date 2021/6/19 16:19
 *  消息在手动应答时是不丢失的,放回到队列中重新消费
 */
public class Task2 {

    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();

        //队列是否持久化(注意: 并不是消息持久化!!!!)
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String next = sc.next();
            //MessageProperties.PERSISTENT_TEXT_PLAIN消息持久化
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, next.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息: " + next);
        }

    }

}
