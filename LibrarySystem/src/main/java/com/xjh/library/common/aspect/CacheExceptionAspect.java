package com.xjh.library.common.aspect;

import com.xjh.library.common.annotation.CacheException;
import com.xjh.library.common.api.ResultCode;
import com.xjh.library.common.exception.MyException;
import com.xjh.library.common.utils.AopUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class CacheExceptionAspect {
    @Pointcut(value = "execution(public * com.xjh.library..*.*CacheService.*(..))")
    public void cacheAspect(){}

    @Around("cacheAspect()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        try {
            result = point.proceed();
        } catch (Throwable throwable){
            // 有CacheException注解的方法需要抛出异常
            if(AopUtil.getAnnotation(point, CacheException.class) != null){
                throw new MyException(ResultCode.CACHE_ERROR.getCode(), ResultCode.CACHE_ERROR.getMessage() + ":" + throwable.getMessage());
            }else{
                log.error(ResultCode.CACHE_ERROR.getMessage() + ":" + throwable.getMessage());
            }
        }
        return result;
    }
}
