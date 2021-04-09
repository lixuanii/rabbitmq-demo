package com.yunfan.rabbitmqdemo.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author lixuan
 * @since 2021-04-08
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@ApiModel(value = "Msg对象", description = "")
public class Msg implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息id")
    @TableId("msg_id")
    private String msgId;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "消息标题")
    private String title;

    @ApiModelProperty(value = "消息接收者")
    @TableField("`to`")
    private String to;

    @ApiModelProperty(value = "消息发送者")
    @TableField("`from`")
    private String from;


}
