package com.starbug.rabbitmq.three;

import com.rabbitmq.client.Channel;
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

        channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String next = sc.next();
            channel.basicPublish("", TASK_QUEUE_NAME, null, next.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息: " + next);
        }

    }

}
