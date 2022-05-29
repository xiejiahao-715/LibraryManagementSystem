package com.xjh.library.common.service;

import com.xjh.library.common.entity.UserResource;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserResourceService extends IService<UserResource> {

    /**
     * 根据id获取用户的图书资源信息
     * @param userId 用户id
     * @return 不存在则返回null
     */
    UserResource getUserResourceById(Long userId);
}
