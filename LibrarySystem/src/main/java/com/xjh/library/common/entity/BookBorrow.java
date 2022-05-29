package com.xjh.library.common.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 图书的借阅信息表
 */
@Data
@TableName(value ="book_borrow")
public class BookBorrow implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 借阅的唯一订单id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 借阅者的用户id
     */
    private Long userId;

    /**
     * 借阅的图书id
     */
    private Long bookId;

    /**
     * 借书的时间
     */
    private LocalDateTime borrowBookDate;

    /**
     * 还书的时间
     */
    private LocalDateTime returnBookDate;

    /**
     * 借阅的时长(单位：天)
     */
    private Integer borrowTime;

    /**
     * 借阅订单完成的时间
     */
    private LocalDateTime endDate;

    /**
     * 借阅订单的状态(0:借阅中,1:续借中,2:完成订单,3:超时未归还))
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