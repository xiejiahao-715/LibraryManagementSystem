package com.xjh.library.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjh.library.common.entity.UserResource;
import com.xjh.library.common.service.UserResourceService;
import com.xjh.library.common.mapper.UmsResourceMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserResourceServiceImpl extends ServiceImpl<UmsResourceMapper, UserResource>
    implements UserResourceService {

    @Override
    @Transactional(readOnly = true)
    public UserResource getUserResourceById(Long userId) {
        LambdaQueryWrapper<UserResource> resourceWrapper = new LambdaQueryWrapper<>();
        resourceWrapper.eq(UserResource::getUserId,userId);
        resourceWrapper.select(UserResource::getUserId,UserResource::getBorrowedNum,UserResource::getBorrowMaxNum);
        return this.getOne(resourceWrapper);
    }
}




