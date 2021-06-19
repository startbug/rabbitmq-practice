package com.starbug.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.starbug.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 *  @Author Starbug
 *  @Date 2021/6/19 17:15
 *  确认发布模式
 *      1.单个确认
 *      2.批量确认
 *      3.异步确认
 */
public class ConfirmMessage {

    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws IOException, InterruptedException {
        //1.单个确认
//        ConfirmMessage.publishMessageIndividually();//发布1000个单独确认消息,耗时:1522ms
        //2.批量确认
//        ConfirmMessage.publishMessageBatch();//发布1000个批量确认消息,耗时:173ms
        //3.异步确认
        ConfirmMessage.publishMessageAsync();//发布1000个批量确认消息,耗时:61ms
    }

    private static void publishMessageIndividually() throws IOException, InterruptedException {
        Channel channel = RabbitMQUtils.getChannel();
        //声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();

        long start = System.currentTimeMillis();

        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            String message = String.valueOf(i);
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
            channel.waitForConfirms();
        }

        long end = System.currentTimeMillis();
        System.out.println("发布1000个单独确认消息,耗时:" + (end - start) + "ms");
    }

    private static void publishMessageBatch() throws IOException, InterruptedException {
        Channel channel = RabbitMQUtils.getChannel();
        //声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();

        int batchSize = 100;

        long start = System.currentTimeMillis();

        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            String message = String.valueOf(i);
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));

            if (i % batchSize == 0) {
                channel.waitForConfirms();
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("发布1000个批量确认消息,耗时:" + (end - start) + "ms");
    }

    private static void publishMessageAsync() throws IOException, InterruptedException {
        Channel channel = RabbitMQUtils.getChannel();
        //声明队列
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();

        //添加监听器,进行消息的确认
        channel.addConfirmListener((deliveryTag, multiple) -> {
            //监听发送成功的消息
            System.out.println("确认的消息:" + deliveryTag);
        }, (deliveryTag, multiple) -> {
            //监听发送失败的消息
            System.out.println("未确认的消息:" + deliveryTag);
        });

        long start = System.currentTimeMillis();

        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            String message = String.valueOf(i);
            channel.basicPublish("", queueName, null, message.getBytes(StandardCharsets.UTF_8));
        }

        long end = System.currentTimeMillis();
        System.out.println("发布1000个批量确认消息,耗时:" + (end - start) + "ms");
    }

}
