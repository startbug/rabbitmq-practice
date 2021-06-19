package com.starbug.rabbitmq.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.starbug.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *  @Author Starbug
 *  @Date 2021/6/19 23:27
 */
public class EmitLogsTopic {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitMQUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        Map<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit", "被队列Q1和Q2接收");
        bindingKeyMap.put("lazy.orange.elephant", "被队列Q1和Q2接收");
        bindingKeyMap.put("quick.orange.fox", "被队列Q1接收");
        bindingKeyMap.put("lazy.brown.fox", "被队列Q2接收");
        bindingKeyMap.put("lazy.pink.rabbit", "满足两个routingKey，但是只会被接受一次");
        bindingKeyMap.put("quick.brown..fox", "不匹配任何队列，丢弃");
        bindingKeyMap.put("quick.orange.male.rabbit", "不匹配，丢弃");
        bindingKeyMap.put("lazy.orange.male.rabbit", "送到Q2");

        Iterator<Map.Entry<String, String>> iterator = bindingKeyMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String routingKey = next.getKey();
            String message = next.getValue();
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("消息内容" + message);
        }

    }

}
