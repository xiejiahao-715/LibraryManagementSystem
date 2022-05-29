package com.xjh.library.module.security.entity;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

// 注册的表单对象
@Data
public class RegisterFormVo {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    @Email(message = "邮箱格式不正确")
    private String email;
}
