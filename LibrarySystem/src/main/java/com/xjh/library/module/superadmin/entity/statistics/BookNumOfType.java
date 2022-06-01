package com.xjh.library.module.superadmin.entity.statistics;

import lombok.Data;

// 一种图书分类对应图书的数量
@Data
public class BookNumOfType {

    // 图书分类id
    private Long typeId;

    // 图书分类的名称
    private String typeName;

    // 图书的数量
    private String num;
}
