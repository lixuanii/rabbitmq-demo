package com.yunfan.rabbitmqdemo.service;

import com.yunfan.rabbitmqdemo.pojo.Msg;

/**
 * @author : lixuan
 * @date : 2021/04/07/10:55
 * @description: 生产者
 */
public interface ProducerService {

    /**
     * 发送消息
     *
     * @param msg          消息体
     * @param exchangeName 队列名称
     * @param routingKey   路由key
     * @return
     */
    void sendMsg(Msg msg, String exchangeName, String routingKey);

    /**
     * 设置消息过期时间
     *
     * @param msg
     * @param exchangeName
     * @param routingKey
     * @return
     */
    void ttlMsgSendMsg(Msg msg, String exchangeName, String routingKey);


    /**
     * 发送消息
     *
     * @param msg          消息体
     * @param exchangeName 队列名称
     * @param routingKey   路由key
     * @return
     */
    void testSendMsg(Msg msg, String exchangeName, String routingKey);
}
