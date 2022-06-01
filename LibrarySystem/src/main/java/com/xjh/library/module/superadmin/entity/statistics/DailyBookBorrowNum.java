package com.xjh.library.module.superadmin.entity.statistics;

import lombok.Data;

import java.time.LocalDate;

// 图书的日借阅量
@Data
public class DailyBookBorrowNum {

    // 统计的具体日期
    private LocalDate date;

    // 当天图书总的借阅量
    private Long num;
}
