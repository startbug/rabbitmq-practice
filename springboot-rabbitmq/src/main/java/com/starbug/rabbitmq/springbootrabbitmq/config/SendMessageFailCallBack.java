package com.starbug.rabbitmq.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 *  @Author Starbug
 *  @Date 2021/6/25 23:22
 *
 */
@Slf4j
@Configuration
public class SendMessageFailCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * @PostConstruct注解会在其他注解执行后再执行
     **/
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机确认回调方法
     * 1.发送消息 交换机成功接收 回调
     *  1.1 CorrelationData保回调消息的ID及相关信息
     *  1.2 交换机收到消息，返回ack=true
     *  1.3 cause为null
     * 2.发消息 交换机接收失败 回调
     *  2.1 CorrelationData保存回调消息的ID及相关信息
     *  2.2 交换机接收到消息 ack=false
     *  2.3 cause返回失败的原因
     **/
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        ReturnedMessage message = correlationData.getReturned();
//        String msg = new String(message.getMessage().getBody());
        String msg = null;
        if (ack) {
            log.info("交换机已经收到ID为{}的消息，内容为{}", correlationData.getId(), msg);
        } else {
            log.info("交换机还没收到ID为{}的消息，内容为{}，原因为{}", correlationData.getId(), msg, cause);
        }
    }

    /**
     * 可以在消息传递过程中不可达目的地时将消息会退给生产者
     * 只有在不可达目的地的时候，才会回退（也就是说，只有失败了，才会执行该方法）
     **/
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.error("消息:{}，被交换机{}回退，回退原因：{}，路由键：{}，应答码：{}",
                new String(returned.getMessage().getBody()),
                returned.getExchange(),
                returned.getReplyText(),
                returned.getRoutingKey(),
                returned.getReplyCode());
    }
}
