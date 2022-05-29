package com.xjh.library.common.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 图书的预定信息表
 */
@Data
@TableName(value ="book_reserve")
public class BookReserve implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 预定的唯一订单id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 预定者的用户id
     */
    private Long userId;

    /**
     * 预定的图书id
     */
    private Long bookId;

    /**
     * 预约的开始时间
     */
    private LocalDateTime reserveTime;

    /**
     * 通知时间
     */
    private LocalDateTime notifyTime;

    /**
     * 预定订单的状态(0:预定中,未通知;1:已通知)
     */
    private Integer status;

    /**
     * 逻辑删除字段
     */
    @TableLogic
    @TableField(select = false)
    private Boolean deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT,select = false)
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,select = false)
    private LocalDateTime gmtUpdate;
}