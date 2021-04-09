package com.yunfan.rabbitmqdemo.service.impl;

import com.yunfan.rabbitmqdemo.pojo.Msg;
import com.yunfan.rabbitmqdemo.service.ProducerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author : lixuan
 * @date : 2021/04/07/10:57
 * @description: 消息生产者
 */
@Service
public class ProducerServiceImpl implements ProducerService {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public void sendMsg(Msg msg, String exchangeName, String routingKey) {
        if (StringUtils.isBlank(msg.getMsgId())) {
            String msgId = UUID.randomUUID().toString().replaceAll("-", "");
            msg.setMsgId(msgId);
        }
        CorrelationData correlationData = new CorrelationData(msg.getMsgId());
        //发送消息到rabbitMQ
        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg, correlationData);

    }

    @Override
    public void ttlMsgSendMsg(Msg msg, String exchangeName, String routingKey) {
        if (StringUtils.isBlank(msg.getMsgId())) {
            String msgId = UUID.randomUUID().toString().replaceAll("-", "");
            msg.setMsgId(msgId);
        }

        CorrelationData correlationData = new CorrelationData(msg.getMsgId());

        //给消息设置过期时间
        MessagePostProcessor postProcessor = message -> {
            message.getMessageProperties().setContentType("application/json");
            message.getMessageProperties().setExpiration("5000");
            message.getMessageProperties().setContentEncoding("utf-8");
            return message;
        };

        //发送消息到rabbitMQ
        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg, postProcessor, correlationData);

    }

    @Override
    public void testSendMsg(Msg msg, String exchangeName, String routingKey) {
        if (StringUtils.isBlank(msg.getMsgId())) {
            String msgId = UUID.randomUUID().toString().replaceAll("-", "");
            msg.setMsgId(msgId);
        }
        CorrelationData correlationData = new CorrelationData(msg.getMsgId());
        //发送消息到rabbitMQ
        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg, correlationData);
    }

}
