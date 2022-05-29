package com.xjh.library.common.api;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

// 统一的返回结果
@Data
public class CommonResult {
    // 请求是否成功
    private boolean success;
    // 状态码
    private Integer code;
    // 提示信息
    private String message;
    // 数据封装
    private Map<String,Object> data = new HashMap<>();

    private CommonResult(){}
    // 成功静态方法
    public static CommonResult success(){
        CommonResult result = new CommonResult();
        result.success = true;
        result.code = ResultCode.SUCCESS.getCode();
        result.message = ResultCode.SUCCESS.getMessage();
        return result;
    }
    // 请求失败的静态方法
    public static CommonResult error(){
        CommonResult result = new CommonResult();
        result.success = false;
        result.code = ResultCode.ERROR.getCode();
        result.message = ResultCode.ERROR.getMessage();
        return result;
    }
    public static CommonResult build(){
        return new CommonResult();
    }
    // 设置请求是否成功
    public CommonResult success(boolean success){
        this.success = success;
        return this;
    }
    // 设置状态码
    public CommonResult code(Integer code){
        this.code = code;
        return this;
    }
    // 设置提示信息
    public CommonResult message(String message){
        this.message = message;
        return this;
    }
    // 添加数据
    public CommonResult data(String key, Object value){
        this.data.put(key,value);
        return this;
    }
    // 设置数据
    public CommonResult data(Map<String,Object> map){
        if(map != null){
            this.data = map;
        }
        return this;
    }
}
