package com.starbug.rabbitmq.fanout;

import com.rabbitmq.client.Channel;
import com.starbug.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;

/**
 *  @Author Starbug
 *  @Date 2021/6/19 22:54
 */
public class ReceiveLog2 {

    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();
        /**
         * 声明一个队列,临时队列
         * 生成一个临时队列,队列名称是随机的
         * 当消费者与队列断开连接后,队列就会自动删除
         **/
        String queueName = channel.queueDeclare().getQueue();

        //绑定交换机与队列
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("等待接收消息...");

        channel.basicConsume(queueName, true, (consumerTag, message) -> {
            System.out.println("Logs2接收到消息:" + new String(message.getBody()));
        }, consumerTag -> {
            System.out.println("失败回调");
        });
    }

}
