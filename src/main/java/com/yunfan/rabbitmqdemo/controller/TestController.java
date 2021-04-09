package com.yunfan.rabbitmqdemo.controller;

import com.yunfan.rabbitmqdemo.constant.RabbitConstants;
import com.yunfan.rabbitmqdemo.constant.TtlMsgRabbitConstants;
import com.yunfan.rabbitmqdemo.pojo.Msg;
import com.yunfan.rabbitmqdemo.service.ProducerService;
import com.yunfan.rabbitmqdemo.constant.TtlQueueRabbitConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : lixuan
 * @date : 2021/04/07/13:38
 * @description: TestController
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ProducerService producerService;

    @RequestMapping("/ack")
    public void testSendMsg(@RequestParam String content, @RequestParam String title) {
        Msg msg = new Msg();
        msg.setContent(content);
        msg.setTitle(title);
        producerService.sendMsg(msg, RabbitConstants.TEST_FANOUT_EXCHANGE, "");
    }



    @RequestMapping("/producer")
    public void sendMsg(@RequestParam String content, @RequestParam String title) {
        Msg msg = new Msg();
        msg.setContent(content);
        msg.setTitle(title);
        producerService.sendMsg(msg, RabbitConstants.FANOUT_EXCHANGE, "");
    }


    @RequestMapping("/ttl/producer")
    public void ttlSendMsg(@RequestParam String content, @RequestParam String title) {
        Msg msg = new Msg();
        msg.setContent(content);
        msg.setTitle(title);
        producerService.sendMsg(msg, TtlQueueRabbitConstants.TTL_QUEUE_DIRECT_EXCHANGE, TtlQueueRabbitConstants.TTL_QUEUE_DIRECT_ROUTING_KEY);
    }


    @RequestMapping("/ttl/msg/producer")
    public void ttlMsgSendMsg(@RequestParam String content, @RequestParam String title) {
        Msg msg = new Msg();
        msg.setContent(content);
        msg.setTitle(title);
        producerService.ttlMsgSendMsg(msg, TtlMsgRabbitConstants.TTL_MSG_DIRECT_EXCHANGE, TtlMsgRabbitConstants.TTL_MSG_DIRECT_ROUTING_KEY);
    }




}
