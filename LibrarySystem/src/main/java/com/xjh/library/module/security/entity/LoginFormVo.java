package com.xjh.library.module.security.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

// 登录的表单对象
@Data
public class LoginFormVo {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

}
