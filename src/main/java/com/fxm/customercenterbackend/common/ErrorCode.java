package com.fxm.customercenterbackend.common;

/**
 * @author fansheng
 */
public enum ErrorCode {
    SUCCESS("0","请求成功",""),
    PARAMS_ERROR("40001","请求参数异常",""),
    NULL_ERROR("40002","请求参数为空",""),
    NOT_LOGIN("40003","未登录",""),
    NO_AUTH("40004","权限不足",""),
    SYSTEM_ERROR("40005","系统内部错误","");


    private final String code;
    private final String message;
    private final String description;


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    ErrorCode(String code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
}
