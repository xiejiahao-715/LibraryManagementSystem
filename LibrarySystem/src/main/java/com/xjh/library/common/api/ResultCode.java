package com.xjh.library.common.api;

public enum ResultCode {
    SUCCESS(200,"请求成功"),
    ERROR(404,"请求失败"),
    SYSTEM_ERROR(500,"服务器内部异常(处理业务逻辑之外没有预料到的错误)"),
    VALID_ERROR(204,"表单验证异常"),
    USER_RE_LOGIN(1000,"用户登录状态异常，需要重新登录"),
    CACHE_ERROR(1010,"缓存出错"),
    No_PERMISSION(1020,"用户无权限");

    private final Integer code;
    private final String message;
    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public Integer getCode() {
        return this.code;
    }
    public String getMessage(){
        return this.message;
    }
}
