package com.xjh.library.module.admin.entity;

import lombok.Data;

// 借阅者对象
@Data
public class BorrowerInfoVo {
    // 图书借阅者 用户id
    private Long id;

    // 用户名称
    private String username;

    // 用户的邮箱
    private String email;

    // 用户状态
    private Integer status;

    // 状态描述
    private String description;

    // 每个用户能借阅的最多图书数量
    private Integer borrowMaxNum;

    // 用户已经借阅图书的数量
    private Integer borrowedNum;
}
