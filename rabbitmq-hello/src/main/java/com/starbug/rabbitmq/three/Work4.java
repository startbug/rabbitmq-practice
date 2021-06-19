package com.starbug.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.starbug.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 *  @Author Starbug
 *  @Date 2021/6/19 16:23
 *  当C2接收到消息的途中当即,那么RabbitMQ就不会收到手动应答,消息不会被删除,重新进入队列,进入到正常运行的服务中进行消费
 */
public class Work4 {

    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();

        System.out.println("C2等待时间较长");
        //采用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, (consumerTag, message) -> {
            try {
                TimeUnit.SECONDS.sleep(30);
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
