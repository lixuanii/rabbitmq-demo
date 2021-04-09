package com.yunfan.rabbitmqdemo.config;

import com.yunfan.rabbitmqdemo.constant.DeadTtlQueueRabbitConstants;
import com.yunfan.rabbitmqdemo.constant.TtlQueueRabbitConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : lixuan
 * @date : 2021/04/07/14:50
 * @description: ttl队列  队列过期时间  设置5s  5秒之后消息将放入死信队列当中
 */
@Configuration
public class TtlQueueRabbitConfig {

    @Bean
    public DirectExchange ttlDirectExchange() {
        /**
         * name 交换机名称
         * durable  是否持久化
         * autoDelete 是否自动删除
         */
        return new DirectExchange(TtlQueueRabbitConstants.TTL_QUEUE_DIRECT_EXCHANGE, true, false);
    }

    /**
     * 队列 过期时间
     * 设置ttl过期时间之后，过了这个时间这个消息就会被移除
     *
     * @return
     */
    @Bean
    public Queue ttlDirectQueueExpiration() {
        /**
         *
         * String name 队列名称
         * boolean durable 是否持久化
         * boolean exclusive 是否独占
         * boolean autoDelete  是否自动删除
         */
        Map<String, Object> args = new HashMap<>();
        //设置队列过期时间为5s
        args.put("x-message-ttl", 5000);
        //过期之后存入到死信队列
        args.put("x-dead-letter-exchange", DeadTtlQueueRabbitConstants.DEAD_TTL_QUEUE_DIRECT_EXCHANGE);
        args.put("x-dead-letter-routing-key", DeadTtlQueueRabbitConstants.DEAD_TTL_QUEUE_DIRECT_ROUTING_KEY);
        return new Queue(TtlQueueRabbitConstants.TTL_QUEUE_DIRECT_QUEUE, true, false, false, args);
    }

    @Bean
    public Binding ttlDirectBinding() {
        return BindingBuilder.bind(ttlDirectQueueExpiration()).to(ttlDirectExchange()).with(TtlQueueRabbitConstants.TTL_QUEUE_DIRECT_ROUTING_KEY);
    }
}
