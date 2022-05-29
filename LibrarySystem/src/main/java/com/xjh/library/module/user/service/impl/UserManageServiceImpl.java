package com.xjh.library.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xjh.library.common.entity.UserMsg;
import com.xjh.library.common.entity.UserResource;
import com.xjh.library.common.service.UserMsgService;
import com.xjh.library.common.service.UserResourceService;
import com.xjh.library.module.security.UserDetailContextHolder;
import com.xjh.library.module.user.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserManageServiceImpl implements UserManageService {

    @Autowired
    private UserResourceService userResourceService;

    @Autowired
    private UserMsgService userMsgService;

    @Override
    @Transactional(readOnly = true)
    public UserResource getResource() {
        Long id = UserDetailContextHolder.getUserId();
        LambdaQueryWrapper<UserResource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserResource::getUserId,id);
        wrapper.select(UserResource::getBorrowedNum,UserResource::getBorrowMaxNum);
        return userResourceService.getOne(wrapper);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserMsg> getUserMsgList() {
        Long uid = UserDetailContextHolder.getUserId();
        LambdaQueryWrapper<UserMsg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserMsg::getUserId,uid);
        wrapper.orderByDesc(UserMsg::getSendDate);
        return userMsgService.list(wrapper);
    }
}
