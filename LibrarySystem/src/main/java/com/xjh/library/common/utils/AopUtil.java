package com.xjh.library.common.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;

public class AopUtil {
    // 获取方法的某一个参数,从0开始算
    public static <T> T getArgs(JoinPoint joinPoint, int n, Class<T> tClass){
        try {
            return tClass.cast(joinPoint.getArgs()[n]);
        } catch (Exception e){
            return null;
        }
    }
    // 获取某一个方法上的注解
    public static <T extends Annotation> T getAnnotation(JoinPoint joinPoint,Class<T> clazz){
        try {
            return ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(clazz);
        } catch (Exception e){
            return null;
        }
    }

    // 获取类上的注解
    public static <T extends Annotation> T getClassAnnotation(JoinPoint joinPoint,Class<T> clazz){
        try {
            return joinPoint.getTarget().getClass().getAnnotation(clazz);
        } catch (Exception e){
            return null;
        }
    }
}
