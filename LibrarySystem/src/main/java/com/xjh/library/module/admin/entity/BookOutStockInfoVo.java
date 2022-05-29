package com.xjh.library.module.admin.entity;

import lombok.Data;

import java.time.LocalDateTime;

// 图书出库的信息对象
@Data
public class BookOutStockInfoVo {

    // 出库记录id
    private Long id;

    // 出库图书id
    private Long bookId;

    // 出库图书名称
    private String bookName;

    // 图书的isbn号
    private String isbn;

    // 操作人员的id
    private Long userId;

    // 操作人员名称
    private String username;

    // 出库的时间
    private LocalDateTime outStockTime;

    // 出库的数量
    private Integer num;
}
