package com.starbug.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.starbug.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 *  @Author Starbug
 *  @Date 2021/6/19 16:23
 */
public class Work3 {

    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();

        System.out.println("C1等待时间较短");

        //当prefetchCount=0的时候,表示为公平分发
        //当prefetchCount=1的时候,表示为不公平分发(能者多劳)
        //当prefetchCount>1的时候,表示为预取值(信道可以存储消息,当消息堆积的时候,信道最多存储prefetchCount条消息)
        int prefetchCount = 2;
        channel.basicQos(prefetchCount);

        //采用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, (consumerTag, message) -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("接收到消息:" + new String(message.getBody()));
            /**
             * 手动应答
             * 1.消息的标记tag
             * 2.是否批量应答
             **/
            //消息的标识
            long deliveryTag = message.getEnvelope().getDeliveryTag();
            channel.basicAck(deliveryTag, false);
        }, consumerTag -> {
            System.out.println(consumerTag + "消费者取消消费回调");
        });

    }

}
