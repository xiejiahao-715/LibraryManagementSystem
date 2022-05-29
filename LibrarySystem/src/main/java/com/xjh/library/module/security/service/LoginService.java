package com.xjh.library.module.security.service;

import com.xjh.library.module.security.entity.LoginFormVo;
import com.xjh.library.module.security.entity.RegisterFormVo;
import com.xjh.library.module.security.entity.UserDetail;

public interface LoginService {
    /**
     * 借阅者登录操作，登录成功则返回token
     * @param loginFormVo 登录的表单
     * @return 用户令牌token
     */
    String login(LoginFormVo loginFormVo);

    /**
     * 获取用户的详细信息
     * @return 返回用户的详细信息
     */
    UserDetail getUserDetail();

    /**
     * 普通用户注册
     */
    void register(RegisterFormVo registerFormVo);

}
