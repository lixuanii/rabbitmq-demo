package com.yunfan.rabbitmqdemo.config;

import com.yunfan.rabbitmqdemo.constant.TtlMsgRabbitConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : lixuan
 * @date : 2021/04/07/15:33
 * @description: ttl 消息过期时间 消息过期之后将会直接移除
 */
@Configuration
public class TtlMsgRabbitConfig {

    @Bean
    public DirectExchange ttlMsgDirectExchange() {
        return new DirectExchange(TtlMsgRabbitConstants.TTL_MSG_DIRECT_EXCHANGE, true, false);
    }

    @Bean
    public Queue ttlMsgDirectQueue() {
        return new Queue(TtlMsgRabbitConstants.TTL_MSG_DIRECT_QUEUE, true, false, false);
    }

    @Bean
    public Binding ttlMsgDirectBinding() {
        return BindingBuilder.bind(ttlMsgDirectQueue()).to(ttlMsgDirectExchange()).with(TtlMsgRabbitConstants.TTL_MSG_DIRECT_ROUTING_KEY);
    }
}
