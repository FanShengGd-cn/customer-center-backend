package com.fxm.customercenterbackend.exception;

import com.fxm.customercenterbackend.common.ErrorCode;

public class BussinessException extends RuntimeException{
    private String code = "40000";
    private String description = "";

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public BussinessException(ErrorCode e) {
        super(e.getMessage());
        this.code = e.getCode();
        this.description = e.getDescription();
    }
    public BussinessException(String message) {
        super(message);
    }
}
