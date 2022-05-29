package com.xjh.library.module.security.entity;

import lombok.Data;

import java.util.List;

// 用户的所有相关对象
@Data
public class UserDetail {

    private Long id;

    private String username;

    private String email;

    // 权限信息列表
    private List<String> permissions;

}
