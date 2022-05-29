package com.xjh.library.module.security.controller;

import com.xjh.library.common.exception.MyException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

// 过滤器中抛出的异常有异常过滤器捕获然后转发到此控制器，由统一异常处理来抛出
@Api(tags = "捕获过滤器异常")
@RestController
@ResponseBody
@RequestMapping("/filterException")
public class FilterExceptionController {

    public static final String MY_FILTER_EXCEPTION_NAME = "filter-myException";

    @ApiOperation("捕获过滤器抛出的MyException异常")
    @RequestMapping("/myException")
    public void filterException(HttpServletRequest request){
        // 获取到异常对象
        Object myException = request.getAttribute(MY_FILTER_EXCEPTION_NAME);
        if(myException instanceof MyException){
            throw  (MyException)myException;
        }
    }
}
