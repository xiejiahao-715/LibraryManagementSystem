package com.xjh.library.common.annotation;


import java.lang.annotation.*;

/**
 * 有该注解的方法上，调用缓存方法时出错，会抛出异常
 * 一般加在缓存方法确保正确执行的方法上,即方法与缓存是强关联的关系，没有了缓存就无法执行下去
 *
 * 如果不加上该注解，缓存出错的时候 则默认不会抛出异常
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheException {
}
