package com.yunfan.rabbitmqdemo.service.impl;

import com.yunfan.rabbitmqdemo.pojo.MsgLog;
import com.yunfan.rabbitmqdemo.dao.MsgLogMapper;
import com.yunfan.rabbitmqdemo.service.MsgLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消息投递日志 服务实现类
 * </p>
 *
 * @author lixuan
 * @since 2021-04-09
 */
@Service
public class MsgLogServiceImpl extends ServiceImpl<MsgLogMapper, MsgLog> implements MsgLogService {

}
