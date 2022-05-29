package com.xjh.library.module.admin.entity.notify;

import lombok.Data;

import java.time.LocalDateTime;

// 预约通知的信息类
@Data
public class ReserveNotifyInfo {

    // 预约订单的id
    private Long reserveId;
    // 用户(借阅者)的id
    private Long userId;
    // 用户预约的时间
    private LocalDateTime reserveTime;
    // 书籍的名字
    private String bookName;
    // 书籍的ISBN号
    private String isbn;
}
