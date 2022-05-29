package com.xjh.library.module.user.entity;

import lombok.Data;

import java.time.LocalDateTime;

// 返回给前端的预约信息对象
@Data
public class ReserveBookInfoVo {

    /**
     * 预定的唯一订单id
     */
    private Long id;

    /**
     * 预定的图书id
     */
    private Long bookId;

    /**
     * 预约图书的名称
     */
    private String bookName;

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
     * 对状态的描述信息
     */
    private String description;
}
