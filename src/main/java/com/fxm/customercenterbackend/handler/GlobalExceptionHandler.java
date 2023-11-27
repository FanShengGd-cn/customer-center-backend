package com.fxm.customercenterbackend.handler;

import com.fxm.customercenterbackend.common.BaseResponse;
import com.fxm.customercenterbackend.common.ResultUtil;
import com.fxm.customercenterbackend.exception.BussinessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BussinessException.class)
    public <T> BaseResponse<T> BussinessExceptionHandler(BussinessException e){
        return new ResultUtil().error(e);
    }

    @ExceptionHandler(RuntimeException.class)
    public <T> BaseResponse<T> RuntimeExceptionHandler(RuntimeException e){
        return new ResultUtil().error(e.getMessage());
    }
}
