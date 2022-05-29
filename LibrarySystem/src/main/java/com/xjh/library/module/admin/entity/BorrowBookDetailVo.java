package com.xjh.library.module.admin.entity;

import lombok.Data;

import java.time.LocalDateTime;

// 管理员 查看的借书详情
@Data
public class BorrowBookDetailVo {
    /**
     * 借阅的唯一订单id
     */
    private Long id;

    /**
     * 借阅的图书id
     */
    private Long bookId;

    /**
     * 图书名称
     */
    private String bookName;

    /**
     * 借书用户的id
     */
    private String userId;

    /**
     * 借书用户的姓名
     */
    private String username;

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
     * 订单状态的描述
     */
    private String description;
}
