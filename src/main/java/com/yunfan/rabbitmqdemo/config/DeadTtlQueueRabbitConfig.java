package com.yunfan.rabbitmqdemo.config;

import com.yunfan.rabbitmqdemo.constant.DeadTtlQueueRabbitConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : lixuan
 * @date : 2021/04/07/15:59
 * @description: 死信队列   队列过期之后将放在这里
 */
@Configuration
public class DeadTtlQueueRabbitConfig {

    @Bean
    public DirectExchange deadTtlQueueDirectExchange() {
        return new DirectExchange(DeadTtlQueueRabbitConstants.DEAD_TTL_QUEUE_DIRECT_EXCHANGE, true, false);
    }

    @Bean
    public Queue deadTtlQueueDirectQueue() {
        return new Queue(DeadTtlQueueRabbitConstants.DEAD_TTL_QUEUE_DIRECT_QUEUE, true);
    }

    @Bean
    public Binding deadTtlQueueDirectBinding() {
        return BindingBuilder.bind(deadTtlQueueDirectQueue()).to(deadTtlQueueDirectExchange()).with(DeadTtlQueueRabbitConstants.DEAD_TTL_QUEUE_DIRECT_ROUTING_KEY);
    }
}
