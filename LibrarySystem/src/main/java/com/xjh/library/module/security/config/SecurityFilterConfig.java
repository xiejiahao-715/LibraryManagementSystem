package com.xjh.library.module.security.config;

import com.xjh.library.module.security.filter.InitUserDetailFilter;
import com.xjh.library.module.security.filter.MyExceptionFilter;
import com.xjh.library.module.security.filter.TokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityFilterConfig {

    // 配置可以直接放行的路径的参数名称
    public static final String EXCLUDE_PATH_NAME = "excludePath";

    @Bean
    public MyExceptionFilter myExceptionFilter(){
        return new MyExceptionFilter();
    }

    @Bean
    public TokenFilter tokenFilter(){
        return new TokenFilter();
    }

    @Bean
    public InitUserDetailFilter initUserDetailFilter(){
        return new InitUserDetailFilter();
    }


    @Bean
    public FilterRegistrationBean<Filter> setMyExceptionFilter(){
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(myExceptionFilter());
        registrationBean.setName("myExceptionFilter");
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(-1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> setTokenFilter(){
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(tokenFilter());
        registrationBean.setName("tokenFilter");
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.addInitParameter(EXCLUDE_PATH_NAME,"/api/login,/api/register");
        registrationBean.setOrder(0);
        return registrationBean;
    }


    @Bean
    public FilterRegistrationBean<Filter> setUserDetailFilter(){
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(initUserDetailFilter());
        registrationBean.setName("initUserDetailFilter");
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.addInitParameter(EXCLUDE_PATH_NAME,"/api/login,/api/register,/api/getUserInfo");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    // 用去过滤器中不需要过滤的路径，路径以 ","间隔
    public static List<String> getExcludePathList(FilterConfig filterConfig){
        if(filterConfig != null){
            String excludePath = filterConfig.getInitParameter(EXCLUDE_PATH_NAME);
            if(excludePath != null){
                return Arrays.asList(excludePath.split(","));
            }
        }
        return null;
    }
    // 判断某一次请求的路径是否为过滤器定义排除的路径
    public static boolean isExcludePath(HttpServletRequest request,List<String> excludePathList){
        if(excludePathList == null){
            return false;
        }
        // 判断该请求是否需要被拦截
        return excludePathList.contains(request.getRequestURI());
    }

}
