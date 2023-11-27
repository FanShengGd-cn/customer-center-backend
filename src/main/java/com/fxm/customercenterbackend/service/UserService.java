package com.fxm.customercenterbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fxm.customercenterbackend.domain.User;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author xiang
* @description 针对表【user】的数据库操作Service
* @createDate 2023-11-27 10:17:49
*/
public interface UserService extends IService<User> {

    boolean registerService(String userAccount, String password, String nickname);

    boolean doLogin(User user, HttpServletRequest req);


    boolean doLogout(HttpServletRequest req);
}
