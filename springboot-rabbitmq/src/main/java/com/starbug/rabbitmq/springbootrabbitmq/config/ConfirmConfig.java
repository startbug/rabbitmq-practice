package com.starbug.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  @Author Starbug
 *  @Date 2021/6/25 23:06
 *  发布确认(高级)
 */
@Configuration
public class ConfirmConfig {

    //交换机
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";

    //队列
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";

    //RoutingKey
    public static final String CONFIRM_ROUTING_KEY = "key1";

    @Bean
    public DirectExchange directExchange() {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).durable(true).build();
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    @Bean
    public Binding binding(DirectExchange directExchange,
                           Queue queue) {
        return BindingBuilder.bind(queue).to(directExchange).with(CONFIRM_ROUTING_KEY);
    }

}
