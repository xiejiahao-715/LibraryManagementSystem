package com.xjh.library.module.security.controller;

import com.xjh.library.common.api.CommonResult;
import com.xjh.library.module.security.entity.LoginFormVo;
import com.xjh.library.module.security.entity.RegisterFormVo;
import com.xjh.library.module.security.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "用户登录操作")
@ResponseBody
@RestController
@RequestMapping("/api/")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @ApiOperation("登录,获取token")
    @PostMapping("/login")
    public CommonResult login(@Valid @RequestBody LoginFormVo loginFormVo){
        String token  = loginService.login(loginFormVo);
        return CommonResult.success().message("登录成功").data("token",token);
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/getUserInfo")
    public CommonResult getUserInfo(){
        return CommonResult.success().message("获取用户信息成功").data("userInfo",loginService.getUserDetail());
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public CommonResult register(@Valid @RequestBody RegisterFormVo registerFormVo){
        loginService.register(registerFormVo);
        return CommonResult.success().message("注册成功");
    }
}
