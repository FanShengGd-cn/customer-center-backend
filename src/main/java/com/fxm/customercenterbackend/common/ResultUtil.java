package com.fxm.customercenterbackend.common;

import com.fxm.customercenterbackend.exception.BussinessException;

/**
 *
 *
 * @author fansheng
 */
public class ResultUtil {
    public static  <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(),data,ErrorCode.SUCCESS.getMessage(),
                ErrorCode.SUCCESS.getDescription());
    }
    public static  <T> BaseResponse<T> success(){
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(),ErrorCode.SUCCESS.getMessage(),
                ErrorCode.SUCCESS.getDescription());
    }
    
    public <T> BaseResponse<T> error(ErrorCode e) {
        return new BaseResponse<>(e.getCode(), e.getMessage(), e.getDescription());
    }

    public <T> BaseResponse<T> error(BussinessException e) {
        return new BaseResponse<>(e.getCode(), e.getMessage(), e.getDescription());
    }
    public <T> BaseResponse<T> error(String msg) {
        return new BaseResponse<>("40000",msg,"");
    }
}
