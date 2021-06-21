package com.starbug.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 *  @Author Starbug
 *  @Date 2021/6/21 21:52
 *  延迟队列,通过rabbitmq插件实现
 *  原理是消息在交换机中停留指定时间,再路由到队列中进行消费
 *  (而之前的是通过在队列中停留指定时间再进行消费)
 */
@Configuration
public class DelayedQueueConfig {

    //队列
    public static final String DELAYED_QUEUE_NAME = "delayed.queue.name";

    //交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange.name";

    //routingKey
    public static final String DELAYED_ROUTING_KEY = "delayed.routing.key";

    @Bean
    public CustomExchange delayedExchange() {
        //因为是安装插件的延迟队列,sdk中并没有,所以需要编写一个自定义的交换机
        /**
         * 1.交换机名称
         * 2.交换机类型
         * 3.是否需要持久化
         * 4.是否需要自动删除
         * 5.其他参数
         **/
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");//延迟类型-指定为直接类型,为什么?因为routingKey是固定的
        //交换机类型x-delayed-message
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, arguments);
    }

    @Bean
    public Queue delayedQueue() {
        return QueueBuilder.durable(DELAYED_QUEUE_NAME).build();
    }

    //绑定
    @Bean
    public Binding delayedQueueBindingDelayedExchange(Queue delayedQueue, CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }

}
