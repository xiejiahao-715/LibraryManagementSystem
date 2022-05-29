package com.xjh.library.common.exception;

import com.xjh.library.common.api.ResultCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

// 自定义异常
@Getter
@Setter
public class MyException extends RuntimeException{
    // 状态码
    private Integer code;
    // 异常信息
    private String message;
    // 携带的异常数据
    private Map<String,Object> data;

    public MyException(String msg){
        super(msg);
        this.message = msg;
        this.code = ResultCode.ERROR.getCode();
        this.data = null;
    }
    public MyException(Integer code,String msg){
        super(msg);
        this.code = code;
        this.message = msg;
        this.data = null;
    }

    public MyException(Integer code,String msg,Map<String,Object> data){
        super(msg);
        this.code = code;
        this.message = msg;
        this.data = data;
    }
}
