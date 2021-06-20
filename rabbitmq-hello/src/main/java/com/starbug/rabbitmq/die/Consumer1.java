package com.starbug.rabbitmq.die;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.starbug.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *  @Author Starbug
 *  @Date 2021/6/20 12:10
 *  死信队列
 *
 */
public class Consumer1 {

    public static final String EXCHANGE_NAME = "normal_exchange";
    public static final String QUEUE_NAME = "normal_queue";
    //死信交换机和死信队列
    public static final String DEAD_EXCHANGE_NAME = "dead_exchange";
    public static final String DEAD_QUEUE_NAME = "dead_queue";

    public static void main(String[] args) throws IOException {

        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        Map<String, Object> arguments = new HashMap();
        //设置过期时间,当正常交换机中指定时间内未消费,就会进入死信队列(消费者端可以指定,生产者也可以,通常在生产者中指定)
//        arguments.put("x-message-ttl", 100000);
        //正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        //设置死信的RoutingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
        //设置正常队列的长度限制,超出6的会放入死信队列中
//        arguments.put("x-max-length", 6);

        channel.queueDeclare(QUEUE_NAME, false, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE_NAME, false, false, false, null);

        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "zhangsan");
        channel.queueBind(DEAD_QUEUE_NAME, DEAD_EXCHANGE_NAME, "lisi");
        System.out.println("等待接收消息....");

        channel.basicConsume(QUEUE_NAME, false, (consumerTag, message) -> {
            String s = new String(message.getBody());
            if ("info5".equals(s)) {
                System.out.println("拒绝消息:" + s);
                //拒绝该消息,并且不放会队列(这里表示不放回普通队列,会进入死信队列中)
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("C1介绍消息:" + new String(message.getBody()));
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        }, consumerTag -> {
        });

    }

}
