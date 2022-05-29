package com.xjh.library.module.user.service;

import com.xjh.library.common.entity.UserMsg;
import com.xjh.library.common.entity.UserResource;

import java.util.List;

// 图书借阅者(普通用户) 有关自身账户的一些操作
public interface UserManageService {

    UserResource getResource();

    // 获取用户的消息列表
    List<UserMsg> getUserMsgList();
}
