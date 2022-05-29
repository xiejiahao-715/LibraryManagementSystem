package com.xjh.library.module.security.filter;

import com.xjh.library.module.security.controller.FilterExceptionController;
import com.xjh.library.common.exception.MyException;

import javax.servlet.*;
import java.io.IOException;

// 用于捕获自定义异常的拦截器
public class MyExceptionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request,response);
        } catch (MyException e){
            // 设置异常信息到request中
            request.setAttribute(FilterExceptionController.MY_FILTER_EXCEPTION_NAME,e);
            request.getRequestDispatcher("/filterException/myException").forward(request, response);
        }
    }
}
