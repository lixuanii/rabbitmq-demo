package com.yunfan.rabbitmqdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lixuan
 */
@SpringBootApplication
@MapperScan(value = {"com.yunfan.rabbitmqdemo.dao","com.baomidou.mybatisplus.core.mapper"})
public class RabbitmqDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqDemoApplication.class, args);
    }

}
