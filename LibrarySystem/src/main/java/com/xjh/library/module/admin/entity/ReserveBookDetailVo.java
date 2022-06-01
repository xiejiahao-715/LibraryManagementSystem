package com.xjh.library.module.admin.entity;

import lombok.Data;

import java.time.LocalDateTime;

// 管理员 查看的预约图书详情
@Data
public class ReserveBookDetailVo {
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
     * 预约用户的id
     */
    private Long userId;

    /**
     * 预约用户的姓名
     */
    private String username;

    /**
     * 预约的开始时间
     */
    private LocalDateTime reserveTime;

    /**
     * 通知时间
     */
    private LocalDateTime notifyTime;

    /**
     * 预约图书的库存信息
     */
    private Integer stockNum;

    /**
     * 预定订单的状态(0:预定中,未通知;1:已通知)
     */
    private Integer status;

    /**
     * 对状态的描述信息
     */
    private String description;
}
