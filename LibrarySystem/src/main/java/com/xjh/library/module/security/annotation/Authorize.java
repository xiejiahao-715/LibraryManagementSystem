package com.xjh.library.module.security.annotation;

import java.lang.annotation.*;

// 认证注解，传入需要拥有的权限和角色名称，只有拥有相应权限或角色的用户才能通过请求
@Documented
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Authorize {
    // 如果采用默认值不配置任何权限或角色限制，则默认都可以通过
    String[] value() default {};
}
