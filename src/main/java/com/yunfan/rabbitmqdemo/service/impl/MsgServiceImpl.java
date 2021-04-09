package com.yunfan.rabbitmqdemo.service.impl;

import com.yunfan.rabbitmqdemo.pojo.Msg;
import com.yunfan.rabbitmqdemo.dao.MsgMapper;
import com.yunfan.rabbitmqdemo.service.MsgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lixuan
 * @since 2021-04-08
 */
@Service
public class MsgServiceImpl extends ServiceImpl<MsgMapper, Msg> implements MsgService {

}
