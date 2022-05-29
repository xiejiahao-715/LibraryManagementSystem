package com.xjh.library.module.user.entity;

import lombok.Data;

// 查看图书信息的表单对象
@Data
public class QueryBookFormVo {
    private String isbn;
    private String author;
    private String name;
    private Long typeId;

    /**
     * 值为null 代表不排序
     * 值为1 代表按照出版日期排序
     * 值为2 代表按照更新时间排序
     */
    private Integer sort;
}
