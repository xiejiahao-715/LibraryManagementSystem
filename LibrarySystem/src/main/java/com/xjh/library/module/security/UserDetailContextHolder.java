package com.xjh.library.module.security;

import com.xjh.library.module.security.entity.UserDetail;

// 在一次请求中存储用户信息的上下文
public class UserDetailContextHolder {
    private static final ThreadLocal<UserDetail> userDetailThreadLocal = new ThreadLocal<>();

    public static void setContext(UserDetail userDetail){
        userDetailThreadLocal.set(userDetail);
    }

    // 请注意一定要注意要在请求结束之前调用此方法，防止内存泄露
    public static void clearContext(){
        userDetailThreadLocal.remove();
    }

    public static UserDetail getContext(){
        return userDetailThreadLocal.get();
    }

    // 从上下文信息中获取用户id
    public static Long getUserId(){
        UserDetail userDetail =  getContext();
        return userDetail == null ? null : userDetail.getId();
    }
}
