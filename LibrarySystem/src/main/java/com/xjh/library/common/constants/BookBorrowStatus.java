package com.xjh.library.common.constants;

// 借书订单的状态码
public enum BookBorrowStatus {

    BORROWING(0,"正在借阅中"),
    RE_BORROW(1,"续借中"),
    FINISH(2,"订单完成"),
    TIMEOUT(3,"超时未归还图书");

    private final Integer statusCode;
    private final String message;

    BookBorrowStatus(Integer code,String msg){
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
