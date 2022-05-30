package com.xjh.library.module.superadmin.entity;

import lombok.Data;

// 普通管理员的信息
@Data
public class AdminInfoVo {
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
}
