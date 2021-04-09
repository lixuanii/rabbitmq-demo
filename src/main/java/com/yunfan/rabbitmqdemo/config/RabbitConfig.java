package com.yunfan.rabbitmqdemo.config;

import com.yunfan.rabbitmqdemo.constant.RabbitConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : lixuan
 * @date : 2021/04/06/11:14
 * @description: RabbitConfig
 */
@Configuration
@Slf4j
public class RabbitConfig {


    @Autowired
    private CachingConnectionFactory connectionFactory;

    /**
     * rabbitTemplate
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        //消息是否成功发送到Exchange
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("消息成功发送至Exchange");
            } else {
                log.info("消息发送到Exchange失败, {}, cause: {}", correlationData, cause);
            }
        });
        //触发setReturnCallback回调必须设置mandatory=true, 否则Exchange没有找到Queue就会丢弃掉消息, 而不会触发回调
        rabbitTemplate.setMandatory(true);
        //失败回调 消息是否从Exchange路由到Queue (只有消息从Exchange路由到Queue失败才会回调这个方法)
        rabbitTemplate.setReturnsCallback(returned ->
                log.info("消息从Exchange路由到Queue失败: exchange: {}, route: {}, replyCode: {}, replyText: {}, message: {}", returned.getExchange(), returned.getRoutingKey(), returned.getReplyCode(), returned.getReplyText(), returned.getMessage())
        );
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 创建交换机
     * 创建队列
     * 队列绑定交换机
     */

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(RabbitConstants.FANOUT_EXCHANGE, true, false);
    }

    @Bean
    public Queue fanoutQueue() {
        return new Queue(RabbitConstants.FANOUT_QUEUE_A, true, false, false);
    }

    @Bean
    public Binding fanoutBinding() {
        return BindingBuilder.bind(fanoutQueue()).to(fanoutExchange());
    }

}
