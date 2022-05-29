package com.xjh.library.module.security.cache;

import com.xjh.library.module.security.entity.UserDetail;

public interface UserDetailCacheService {
    /**
     * 将UserDetail信息存储到缓存中
     * @param userDetail userDetail信息
     */
    void setUserDetail(UserDetail userDetail);

    /**
     * 从缓存中获取到UserDetail信息
     * @param uid 用户的唯一id
     * @return 返回UserDetail信息
     */
    UserDetail getUserDetail(Long uid);

    /**
     * 从缓存中请求UserDetail信息
     * @param uid 用户的唯一id
     */
    void clearUserDetail(Long uid);
}
