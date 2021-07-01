package com.starbug.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Starbug
 * @Date 2021/6/25 23:06
 * 发布确认(高级)
 */
@Configuration
public class ConfirmConfig {

    //交换机
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";

    //队列
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";

    //RoutingKey
    public static final String CONFIRM_ROUTING_KEY = "key1";

    //备份交换机
    public static final String BACKUP_EXCHANGE = "backup.exchange";

    //备份队列
    public static final String BACKUP_QUEUE = "backup.queue";

    //警告队列
    public static final String WARNING_QUEUE = "warning.queue";

    @Bean
    public DirectExchange directExchange() {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                //给交换机绑定一个备份交换机,备份交换机绑定一个备份队列和一个警告队列
                .withArgument("alternate-exchange", BACKUP_EXCHANGE)
                .build();
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

    @Bean
    public FanoutExchange backupFanoutExchange() {
        return ExchangeBuilder.fanoutExchange(BACKUP_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE).build();
    }

    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE).build();
    }

    @Bean
    public Binding backupQueueBindingBackupExchange(Queue backupQueue, FanoutExchange backupFanoutExchange) {
        return BindingBuilder.bind(backupQueue).to(backupFanoutExchange);
    }

    @Bean
    public Binding warningQueueBindingBackupExchange(Queue warningQueue, FanoutExchange backupFanoutExchange) {
        return BindingBuilder.bind(warningQueue).to(backupFanoutExchange);
    }

}
