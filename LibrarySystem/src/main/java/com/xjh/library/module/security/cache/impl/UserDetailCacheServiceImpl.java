package com.xjh.library.module.security.cache.impl;

import com.xjh.library.common.annotation.CacheException;
import com.xjh.library.common.cache.RedisService;
import com.xjh.library.module.security.entity.UserDetail;
import com.xjh.library.module.security.cache.UserDetailCacheService;
import com.xjh.library.module.security.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailCacheServiceImpl implements UserDetailCacheService {

    @Autowired
    private RedisService redisService;

    private String getUserDetailKey(Long id){
        return "library-user-" + id;
    }

    @Override
    @CacheException
    public void setUserDetail(UserDetail userDetail) {
        Long id = userDetail.getId();
        redisService.set(getUserDetailKey(id),userDetail, JwtTokenUtil.TOKEN_EXPIRE_TIME);
    }

    @Override
    public UserDetail getUserDetail(Long uid) {
        return redisService.get(getUserDetailKey(uid),UserDetail.class);
    }

    @Override
    public void clearUserDetail(Long uid) {
        redisService.del(getUserDetailKey(uid));
    }
}
