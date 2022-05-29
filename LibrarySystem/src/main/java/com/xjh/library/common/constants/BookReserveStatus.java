package com.xjh.library.common.constants;

// 图书预定订单的状态
public enum BookReserveStatus {

    RESERVING(0,"正在预定中"),
    NOTIFIED(1,"已通知");

    private final Integer statusCode;
    private final String message;

    BookReserveStatus(Integer code,String msg){
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
