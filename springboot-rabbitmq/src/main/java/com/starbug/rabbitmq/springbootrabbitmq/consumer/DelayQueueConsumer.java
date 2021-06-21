package com.starbug.rabbitmq.springbootrabbitmq.consumer;

import com.starbug.rabbitmq.springbootrabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 *  @Author Starbug
 *  @Date 2021/6/21 22:05
 *  消费者,基于插件的延迟消息
 */
@Slf4j
@Component
public class DelayQueueConsumer {

    //监听消息
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void receiveDelayQueue(Message message) {
        String body = new String(message.getBody());
        log.info("当前时间:{},收到延迟消息为:{}", new Date().toString(), body);
    }

}
