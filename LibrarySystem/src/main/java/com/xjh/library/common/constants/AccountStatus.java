package com.xjh.library.common.constants;

// 用户账号的状态信息
public enum AccountStatus {

    NORMAL(0,"用户正常使用"),
    DISABLED(1,"被封禁,无法使用"),
    LOG_OFF(2,"账户已注销");


    private final Integer statusCode;
    private final String message;

    AccountStatus(Integer code,String msg){
        this.statusCode = code;
        this.message = msg;
    }

    public Integer getStatusCode(){
        return this.statusCode;
    }

    public String getMessage(){
        return this.message;
    }
}
