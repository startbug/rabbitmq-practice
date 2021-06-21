package com.starbug.rabbitmq.springbootrabbitmq.controller;

import com.starbug.rabbitmq.springbootrabbitmq.config.DelayedQueueConfig;
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
 *  @Date 2021/6/20 22:31
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable("message") String message) {
        log.info("当前时间:{},发送消息给两个TTL队列:{}", new Date().toString(), message);

        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttl为10s的队列:" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttl为40s的队列:" + message);
    }

    /**
     * @Author lhh
     * @Description 添加自定义制定延迟时间的队列
     * 这样会有一个问题,如果通过在消息属性设置TTL的方式,消息可能不会按时死亡,因为RabbitMQ只会检查第一个消息是否过期,
     * 如果过期就丢入到死信队列中,如果第一个消息延时很长很长,第二个消息延时很短,第二个消息也不会立即被执行,等到第一个消息消费后才会消费第二个消息
     * 这个问题可以通过插件得到解决
     * @Date 2021/6/21 21:37
     * @Param [message, ttlTime]
     * @return void
     **/
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message, @PathVariable String ttlTime) {
        log.info("当前时间:{},发送一条时长为{}毫秒的消息给队列:{}", new Date().toString(), ttlTime, message);
        rabbitTemplate.convertAndSend("X", "XC", message, msg -> {
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable String message, @PathVariable Integer delayTime) {
        log.info("当前时间:{},发送一条时长为{}毫秒的消息给延迟队列:{}", new Date().toString(), delayTime, message);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,
                DelayedQueueConfig.DELAYED_ROUTING_KEY,
                message,
                msg -> {
                    //设置消息的延迟时间,单位依然是毫秒
                    msg.getMessageProperties().setDelay(delayTime);
                    return msg;
                });

    }

}
