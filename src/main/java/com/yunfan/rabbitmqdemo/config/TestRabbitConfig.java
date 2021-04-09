package com.yunfan.rabbitmqdemo.config;

import com.yunfan.rabbitmqdemo.constant.RabbitConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : lixuan
 * @date : 2021/04/08/16:05
 * @description: 测试
 */
@Configuration
public class TestRabbitConfig {

    @Bean
    public FanoutExchange testFanoutExchange() {
        return new FanoutExchange(RabbitConstants.TEST_FANOUT_EXCHANGE, true, false);
    }

    /**
     * 基于消息事务的处理方式，当失败消息进行重试，有时间间隔，当达到超时时，就发送到死信队列，等待人工处理
     *
     * @return
     */
    @Bean
    public Queue testFanoutQueue() {
        //超时2s就放入死信队列
        return QueueBuilder.durable(RabbitConstants.TEST_FANOUT_QUEUE_A).deadLetterExchange(RabbitConstants.TEST_DDL_EXCHANGE).ttl(2000).build();
        //        Map<String, Object> map = new HashMap<>();
//        map.put("x-dead-letter-exchange", RabbitConstants.TEST_DDL_EXCHANGE);
//        return new Queue(RabbitConstants.TEST_FANOUT_QUEUE_A, true, false, false);
    }

    /**
     * 队列绑定到交换机
     * @return
     */
    @Bean
    public Binding testFanoutBinding() {
        return BindingBuilder.bind(testFanoutQueue()).to(testFanoutExchange());
    }


    /**
     * 死信队列
     */

    /**
     * 死信交换机
     * @return
     */
    @Bean
    public FanoutExchange testFanoutDdlExchange() {
        return new FanoutExchange(RabbitConstants.TEST_DDL_EXCHANGE, true, false);
    }

    /**
     * 死信队列
     * @return
     */
    @Bean
    public Queue testFanoutDdlQueue() {
        return new Queue(RabbitConstants.TEST_DDL_QUEUE_A, true, false, false);
    }

    /**
     * 死信队列绑定至交换机
     * @return
     */
    @Bean
    public Binding testFanoutDdlBinding() {
        return BindingBuilder.bind(testFanoutDdlQueue()).to(testFanoutDdlExchange());
    }

}
