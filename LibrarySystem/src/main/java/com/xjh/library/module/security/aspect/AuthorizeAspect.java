package com.xjh.library.module.security.aspect;

import com.xjh.library.common.api.ResultCode;
import com.xjh.library.common.exception.MyException;
import com.xjh.library.common.utils.AopUtil;
import com.xjh.library.module.security.UserDetailContextHolder;
import com.xjh.library.module.security.annotation.Authorize;
import com.xjh.library.module.security.entity.UserDetail;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Aspect
public class AuthorizeAspect {

    // 代理有注解Authorize或者类上标注了Authorize的方法
    @Pointcut(value = "@within(com.xjh.library.module.security.annotation.Authorize)" +
            "|| @annotation(com.xjh.library.module.security.annotation.Authorize)")
    public void authorizeAspect(){}

    @Before("authorizeAspect()")
    public void before(JoinPoint joinPoint){
        // 获取方法上面的注解
        Authorize authorize = AopUtil.getAnnotation(joinPoint,Authorize.class);
        if(authorize == null){
            // 如果方法上未找到注解信息，则去类上寻找
            authorize = AopUtil.getClassAnnotation(joinPoint,Authorize.class);
        }
        // 方法和类上都没有，没有限制授权信息，直接放行
        if(authorize != null){
            List<String> authorization = Arrays.asList(authorize.value());
            if(authorization.size() == 0){
                return;
            }
            UserDetail userDetail = UserDetailContextHolder.getContext();
            if(userDetail == null || userDetail.getPermissions() == null){
                throw new MyException(ResultCode.USER_RE_LOGIN.getCode(), "用户无授权信息");
            }
            // 查看是否拥有权限
            boolean hasPermission = userDetail.getPermissions()
                    .stream().anyMatch(authorization::contains);
            if(!hasPermission){
                throw new MyException(ResultCode.No_PERMISSION.getCode(), ResultCode.No_PERMISSION.getMessage());
            }
        }
    }

}
