package com.yunfan.rabbitmqdemo.enums;

/**
 * @author: lixuan
 * @Date: 2021/02/27 11:33 上午
 * @Description: 活动订单状态
 */
public enum MsgLogStatusEnum {

    ING(0,"投递中 "),
    SUCCESS(1,"投递成功"),
    FAIL(2,"投递失败"),
    USED(3,"已消费"),
    ;

    private final Integer status;
    private final String description;

    MsgLogStatusEnum(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
