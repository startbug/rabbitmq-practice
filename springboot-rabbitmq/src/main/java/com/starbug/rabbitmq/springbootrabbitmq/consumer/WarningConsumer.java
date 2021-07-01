package com.starbug.rabbitmq.springbootrabbitmq.consumer;

import com.starbug.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 *  @Author Starbug
 *  @Date 2021/6/26 21:08
 *  报警消费者,备份交换机会将消息发送到该队列中
 */
@Slf4j
@Component
public class WarningConsumer {

    //接受报警消息
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE)
    public void receiveWarningMsg(Message message) {
        String msg = new String(message.getBody());
        log.error("报警不可路由的消息:{}", msg);
    }

}
