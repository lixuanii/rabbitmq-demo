package com.yunfan.rabbitmqdemo.constant;

/**
 * @author : lixuan
 * @date : 2021/04/06/11:16
 * @description: RabbitConstants
 */
public class DeadTtlQueueRabbitConstants {

    /**
     * 死信队列 ttl direct模式 路由key
     */
    public static final String DEAD_TTL_QUEUE_DIRECT_EXCHANGE = "deadTtlQueueDirectExchange";
    public static final String DEAD_TTL_QUEUE_DIRECT_QUEUE = "deadTtlQueueDirectQueue";
    public static final String DEAD_TTL_QUEUE_DIRECT_ROUTING_KEY = "deadTtlQueueDirectRoutingKey";

}
