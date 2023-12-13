package com.fxm.customercenterbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fxm.customercenterbackend.model.domain.User;
import com.fxm.customercenterbackend.model.request.PageRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author Fansheng
* @description 针对表【user】的数据库操作Service
* @createDate 2023-11-27 10:17:49
*/
public interface UserService extends IService<User> {

    boolean registerService(String userAccount, String password, String nickname);

    boolean doLogin(User user, HttpServletRequest req);

    boolean doLogout(HttpServletRequest req);

    User getLoginUser(HttpServletRequest req);

    List<User> searchPartnerByTags(List<String> tags);
    Page<User> searchPartnerPagesByTags(List<String> tags, PageRequest page);
}
