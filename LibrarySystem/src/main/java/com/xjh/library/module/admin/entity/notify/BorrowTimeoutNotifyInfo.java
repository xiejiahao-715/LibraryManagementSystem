package com.xjh.library.module.admin.entity.notify;

import lombok.Data;

import java.time.LocalDateTime;

// 借阅订单到期的通知类
@Data
public class BorrowTimeoutNotifyInfo {

    // 借阅订单的id
    private Long borrowId;

    // 借阅者的id
    private Long userId;

    // 借书的时间
    private LocalDateTime borrowBookDate;

    // 书籍的名称
    private String bookName;

    // 书籍的ISBN号
    private String isbn;

}
