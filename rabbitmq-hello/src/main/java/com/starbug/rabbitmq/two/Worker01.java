package com.starbug.rabbitmq.two;

import com.rabbitmq.client.Channel;
import com.starbug.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;

/**
 *  @Author Starbug
 *  @Date 2021/6/18 13:24
 *  工作线程（相当于消费者）
 */
public class Worker01 {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("C2等待接收");
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.basicConsume(QUEUE_NAME, true,
                (consumerTag, message) -> {
                    System.out.println("接收到的消息" + new String(message.getBody()));
                }, consumerTag -> {
                    //消息被取消时,会执行下面的代码
                    System.out.println(consumerTag + "消费者取消消费接口的回调逻辑");
                });
    }

}
