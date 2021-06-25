package com.starbug.rabbitmq.springbootrabbitmq.controller;

import com.starbug.rabbitmq.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 *  @Author Starbug
 *  @Date 2021/6/25 23:13
 */
@Slf4j
@RestController
@RequestMapping("/confirm")
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //发消息
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable("message") String message) {
        CorrelationData correlationData1 = new CorrelationData();
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY,
                message,
                correlationData1);
        log.info("发送消息,内容:{},日期:{},RoutingKey为{}", message, new Date(), ConfirmConfig.CONFIRM_ROUTING_KEY);
        log.info("----------------------------------------------------------");
        CorrelationData correlationData2 = new CorrelationData();
        //发送到一个不存在的队列，仍然显示发送成功（成功发送到交换机，而并未发送到队列中，所以并未被消费，消息丢失）
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.CONFIRM_ROUTING_KEY + "xx",
                message,
                correlationData2);
        log.info("发送消息,内容:{},日期:{},RoutingKey为{}", message, new Date(), ConfirmConfig.CONFIRM_ROUTING_KEY + "xx");
    }

}
