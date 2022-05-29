package com.xjh.library.module.user.entity;

import lombok.Data;

import java.time.LocalDate;

// 返回给借阅者的有关图书信息
@Data
public class BookInfoVo {

    // 图书的唯一id
    private Long id;

    // 图书的分类ID
    private Long typeId;

    // 分类名称
    private String typeName;

    // 图书ISBN号
    private String isbn;

    // 图书名称
    private String name;

    //  图书作者
    private String author;

    // 出版社
    private String press;

    // 图书定价
    private String price;

    // 图书出版日期
    private LocalDate publishTime;

    // 图书简介
    private String description;

    // 图书封面图片的链接地址
    private String coverImg;

    // 图书的库存数量
    private Integer stockNum;
}
