package com.yunfan.rabbitmqdemo.constant;

/**
 * @author : lixuan
 * @date : 2021/04/06/11:16
 * @description: RabbitConstants
 */
public class RabbitConstants {

    /**
     * direct模式 路由key
     */
    public static final String DIRECT_EXCHANGE = "directExchange";
    public static final String DIRECT_QUEUE = "directQueue";
    public static final String DIRECT_ROUTING_KEY = "directRoutingKey";


    /**
     * topic模式 主题模式
     */
    public static final String TOPIC_EXCHANGE = "topicExchange";
    public static final String TOPIC_QUEUE = "topicQueue";
    public static final String TOPIC_ROUTING_KEY = "user.#";

    /**
     * fanout模式 广播者
     */
    public static final String FANOUT_EXCHANGE = "fanoutExchange";
    public static final String FANOUT_QUEUE_A = "fanoutQueueA";
    public static final String FANOUT_QUEUE_B = "fanoutQueueB";
    public static final String FANOUT_QUEUE_C = "fanoutQueueC";

    /**
     * fanout模式 广播者 test
     */
    public static final String TEST_FANOUT_EXCHANGE = "testFanoutExchange";
    public static final String TEST_FANOUT_QUEUE_A = "testFanoutQueueA";
    public static final String TEST_FANOUT_QUEUE_B = "testFanoutQueueB";
    public static final String TEST_FANOUT_QUEUE_C = "testFanoutQueueC";

    /**
     * test 死信队列
     */
    public static final String TEST_DDL_EXCHANGE = "testDdlExchange";
    public static final String TEST_DDL_QUEUE_A = "testDdlQueue_A";


}
