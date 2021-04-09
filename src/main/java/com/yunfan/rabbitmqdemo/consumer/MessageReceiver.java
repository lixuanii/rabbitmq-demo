package com.yunfan.rabbitmqdemo.consumer;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.yunfan.rabbitmqdemo.constant.RabbitConstants;
import com.yunfan.rabbitmqdemo.constant.TtlMsgRabbitConstants;
import com.yunfan.rabbitmqdemo.constant.TtlQueueRabbitConstants;
import com.yunfan.rabbitmqdemo.enums.MsgLogStatusEnum;
import com.yunfan.rabbitmqdemo.pojo.Msg;
import com.yunfan.rabbitmqdemo.pojo.MsgLog;
import com.yunfan.rabbitmqdemo.service.MsgLogService;
import com.yunfan.rabbitmqdemo.service.MsgService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


/**
 * @author : lixuan
 * @date : 2021/04/07/11:23
 * @description: 消息接收者
 */
@Component
@Slf4j
public class MessageReceiver {

    @Autowired
    private MsgService msgService;

    @Autowired
    private MsgLogService msgLogService;

    @RabbitListener(queues = RabbitConstants.FANOUT_QUEUE_A)
    public void fanoutQueueReceiver(Message message, Channel channel) throws IOException {

        long tag = message.getMessageProperties().getDeliveryTag();

        try {
            String str = new String(message.getBody());
            //转换为消息实体
            Msg msg = JSON.parseObject(str, Msg.class);
            System.out.println("普通fanout模式 收到消息: {}" + msg.toString());

            //消费确认
            channel.basicAck(tag, false);

        } catch (Exception e) {
            channel.basicNack(tag, false, true);
        }

    }

    @RabbitListener(queues = TtlQueueRabbitConstants.TTL_QUEUE_DIRECT_QUEUE)
    public void ttlQueueDirectReceiver(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String str = new String(message.getBody());
        Msg msg = JSON.parseObject(str, Msg.class);
        try {
            log.info("设置队列过期时间并加入死信队列 收到消息: {0}" + msg);

            String msgId = msg.getMsgId();
            if (StringUtils.isBlank(msgId)) {
                log.info("msgId is null");
                return;
            }

            if (msgService.getById(msgId) != null) {
                //需要查询在数据库中是否存在。保证数据幂等性
                log.info(" id {}已经被消费，无需重复消费", msgId);
                //手动ack mq移除消息
                channel.basicAck(deliveryTag, false);
                return;
            }



            if (msgService.save(msg)) {
                log.info("数据插入成功");
                //正常处理成功 手动签收 消息确认 无需重试
                channel.basicAck(deliveryTag, false);
                return;
            }
        } catch (Exception e) {
            /**
             * 重试机制
             * multiple 是否批量 消息的标识 false只确认当前一个消息收到，true确认所有consumer获得的消息
             * requeue 是否重回队列
             *          true，则消息重新回到queue，broker会重新发送该消息给消费端
             *          false直接丢弃消息
             */
            channel.basicNack(deliveryTag, false, true);
            //消息不重要那么 记录日志  失败消息，后期人工补偿。将消息存放至死信队列，单独写一个死信消费者实现消费
            log.error("出现异常: {0}", e.getMessage());

            e.printStackTrace();
        }
    }

    @RabbitListener(queues = TtlMsgRabbitConstants.TTL_MSG_DIRECT_QUEUE)
    public void ttlMsgDirectReceiver(Message message, Channel channel) throws IOException {
        String str = new String(message.getBody());
        Msg msg = JSON.parseObject(str, Msg.class);
        log.info("设置消息过期时间 收到消息: {0}" + msg);
        MessageProperties properties = message.getMessageProperties();
        long tag = properties.getDeliveryTag();
        if (true) {
            channel.basicAck(tag, false);
        } else {
            channel.basicNack(tag, false, true);
        }
    }


    private static final Integer RETRY = 3;


    @RabbitListener(queues = RabbitConstants.TEST_FANOUT_QUEUE_A)
    public void testMsgReceiver(Message message, Channel channel) throws IOException, InterruptedException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        Msg msg = JSON.parseObject(message.getBody(), Msg.class);
        try {
            int a = 1 / 0;
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            Map<String, Object> headers = message.getMessageProperties().getHeaders();
            //重试次数
            Integer retryCount;
            String mapKey = "retry-count";
            if (!headers.containsKey(mapKey)) {
                retryCount = 0;
            } else {
                retryCount = (Integer) headers.get(mapKey);
            }
            if (retryCount++ < RETRY) {
                log.info("已经重试 " + retryCount + " 次");
                headers.put("retry-count", retryCount);
                //当消息回滚到消息队列时，这条消息不会回到队列尾部，而是仍是在队列头部。
                //这时消费者会立马又接收到这条消息进行处理，接着抛出异常，进行 回滚，如此反复进行
                //而比较理想的方式是，出现异常时，消息到达消息队列尾部，这样既保证消息不回丢失，又保证了正常业务的进行。
                //因此我们采取的解决方案是，将消息进行应答。
                //这时消息队列会删除该消息，同时我们再次发送该消息 到消息队列，这时就实现了错误消息进行消息队列尾部的方案
                //1.应答
                channel.basicAck(deliveryTag, false);
                //2.重新发送到MQ中
                AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().contentType("application/json").headers(headers).build();
                channel.basicPublish(message.getMessageProperties().getReceivedExchange(),
                        message.getMessageProperties().getReceivedRoutingKey(), basicProperties,
                        message.getBody());
            } else {

                log.info("现在重试次数为：" + retryCount);
                /**
                 * 重要的操作存盘
                 * msgService.save(msg);
                 * 手动ack
                 * channel.basicAck(deliveryTag,false);
                 * 通知人工处理
                 * log.error("重试三次异常，快来人工处理");
                 */

                MsgLog msgLog = new MsgLog();
                msgLog.setMsgId(msg.getMsgId());
                msgLog.setMsg(new String(message.getBody(),"utf-8"));
                msgLog.setExchange(message.getMessageProperties().getReceivedExchange());
                msgLog.setRoutingKey(message.getMessageProperties().getReceivedRoutingKey());
                msgLog.setTryCount(retryCount);
                msgLog.setStatus(MsgLogStatusEnum.FAIL.getStatus());
                msgLogService.save(msgLog);

                /**
                 * 不重要的操作放入 死信队列
                 * 消息异常处理：消费出现异常后，延时几秒，然后从新入队列消费，直到达到ttl超时时间，再转到死信，证明这个信息有问题需要人工干预
                 */
                //休眠2s 延迟写入队列，触发转入死信队列
//                Thread.sleep(2000);
//                channel.basicNack(deliveryTag, false, true);
            }
        }
    }


    @RabbitListener(queues = RabbitConstants.TEST_DDL_QUEUE_A)
    public void deadTestReceiver(Message message, Channel channel) throws IOException {
        log.info("消息将放入死信队列", new String(message.getBody(), "UTF-8"));
        String str = new String(message.getBody());
        //转换为消息实体
        Msg msg = JSON.parseObject(str, Msg.class);
        log.info("收到的消息为{}", msg);
    }


}
