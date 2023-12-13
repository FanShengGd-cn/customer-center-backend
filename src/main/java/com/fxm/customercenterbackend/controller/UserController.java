package com.fxm.customercenterbackend.controller;

import com.fxm.customercenterbackend.common.BaseResponse;
import com.fxm.customercenterbackend.common.Constants;
import com.fxm.customercenterbackend.common.ErrorCode;
import com.fxm.customercenterbackend.common.ResultUtil;
import com.fxm.customercenterbackend.model.domain.User;
import com.fxm.customercenterbackend.model.request.UserRegisterRequest;
import com.fxm.customercenterbackend.exception.BussinessException;
import com.fxm.customercenterbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;


    @Operation(summary = "注册")
    @PostMapping("/register")
    public  BaseResponse<Long> register(@RequestBody UserRegisterRequest user ){
        if(user.getUserAccount()==null || user.getNickname() == null || user.getPassword() == null
                || user.getUserAccount().equals("") || user.getPassword().equals("") || user.getNickname().equals(""))
            throw new BussinessException(ErrorCode.NULL_ERROR);
        if(!user.getUserAccount().matches("[A-Za-z0-9_\\-\\u4e00-\\u9fa5]+")) throw new BussinessException("账户信息包含特殊字符");
        boolean res = userService.registerService(user.getUserAccount(), user.getPassword(), user.getNickname());
        if(!res)    throw new BussinessException(ErrorCode.SYSTEM_ERROR);
        return ResultUtil.success(userService.count());
    }
    @PostMapping("/login")
    public BaseResponse<User> login(@RequestBody User user, HttpServletRequest request){
        if(user.getUserAccount()==null || user.getPassword() == null
                || user.getUserAccount().equals("") || user.getPassword().equals(""))
            throw new BussinessException(ErrorCode.NULL_ERROR);
        boolean res = userService.doLogin(user,request);
        if(!res)    throw new BussinessException(ErrorCode.SYSTEM_ERROR);
        return ResultUtil.success((User)request.getSession().getAttribute(Constants.SESSION_USER_ATTR));
    }

    @PostMapping("/logout")
    public <T> BaseResponse<T> logout(HttpServletRequest request){
        boolean res = userService.doLogout(request);
        if(!res)    throw new BussinessException(ErrorCode.SYSTEM_ERROR);
        return ResultUtil.success();
    }

}
