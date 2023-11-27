package com.fxm.customercenterbackend.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {
    private String code;
    private T data;
    private String message;
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BaseResponse(String code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(String code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

}
