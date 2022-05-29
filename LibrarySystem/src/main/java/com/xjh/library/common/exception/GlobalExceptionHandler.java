package com.xjh.library.common.exception;

import com.xjh.library.common.api.CommonResult;
import com.xjh.library.common.api.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

// 全局异常处理
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    // 处理全局的任何异常
    @ExceptionHandler(Exception.class)
    public CommonResult globalError(Exception e){
        log.error(e.getMessage());
        e.printStackTrace();
        return CommonResult.build().success(false).code(ResultCode.SYSTEM_ERROR.getCode()).message(e.getMessage());
    }

    // 处理自定义的异常
    @ExceptionHandler(MyException.class)
    public CommonResult defineError(MyException e){
        log.error(e.getMessage());
        return CommonResult.build().success(false).code(e.getCode()).message(e.getMessage()).data(e.getData());
    }

    // 对表单验证时抛出的 MethodArgumentNotValidException 异常做统一处理
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult validError(MethodArgumentNotValidException e){
        log.error(e.getMessage());
        // 拼接错误信息
        String errorMsg = e.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(","));
        return CommonResult.build().success(false).code(ResultCode.VALID_ERROR.getCode()).message(errorMsg);
    }
}
