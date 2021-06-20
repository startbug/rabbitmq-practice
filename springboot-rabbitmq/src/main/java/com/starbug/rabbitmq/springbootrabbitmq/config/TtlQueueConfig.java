package com.starbug.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 *  @Author Starbug
 *  @Date 2021/6/20 22:19
 *  TTL队列 配置文件类
 */
@Configuration
public class TtlQueueConfig {

    //普通交换机名称
    public static final String X_EXCHANGE = "X";
    //死信交换机名称
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";

    //普通对列名
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    //死信队列名
    public static final String DEAD_LETTER_QUEUE = "QD";

    //声明XEXCHAANGE
    @Bean
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    @Bean
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    //声明队列
    @Bean
    public Queue queueA() {
        Map<String, Object> arguments = new HashMap<>(3);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        //设置TTL,单位:ms
        arguments.put("x-message-ttl", 10000);

        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    //声明队列
    @Bean
    public Queue queueB() {
        Map<String, Object> arguments = new HashMap<>(3);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        //设置TTL,单位:ms
        arguments.put("x-message-ttl", 40000);

        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    //死信队列
    @Bean
    public Queue queueD() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    //绑定普通队列和普通交换机
    @Bean
    public Binding queueABindingX(Queue queueA, Exchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA").noargs();
    }

    @Bean
    public Binding queueBBindingX(Queue queueB, Exchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB").noargs();
    }

    //绑定死信队列和死信交换机
    @Bean
    public Binding queueDBindingY(Queue queueD, Exchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("YD").noargs();
    }


}