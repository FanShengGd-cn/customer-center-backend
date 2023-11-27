package com.fxm.customercenterbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fxm.customercenterbackend.common.ErrorCode;
import com.fxm.customercenterbackend.common.PublicConsts;
import com.fxm.customercenterbackend.domain.User;
import com.fxm.customercenterbackend.exception.BussinessException;
import com.fxm.customercenterbackend.mapper.UserMapper;
import com.fxm.customercenterbackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
* @author fansheng
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-11-27 10:17:49
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
    @Override
    public boolean registerService(String userAccount, String password, String nickname) {
        if(userAccount==null || password==null || nickname == null || userAccount.equals("") || password.equals("")
                || nickname.equals("")) throw new BussinessException(ErrorCode.NULL_ERROR);
        Long count = lambdaQuery().eq(User::getUserAccount, userAccount).count();
        if(count > 0){
            throw new BussinessException("用户信息已存在");
        }
        String md5pwd = DigestUtils.md5DigestAsHex((password + PublicConsts.SALT).getBytes());
        User user = new User();
        user.setUserAccount(userAccount);
        user.setPassword(md5pwd);
        user.setNickname(nickname);
        save(user);
        return true;
    }

    @Override
    public boolean doLogin(User user, HttpServletRequest req) {
        User realUser = lambdaQuery().eq(User::getUserAccount, user.getUserAccount()).one();
        if(realUser == null){
            throw new BussinessException("用户名或密码不正确");
        }
        System.out.println(DigestUtils.md5DigestAsHex((user.getPassword()+PublicConsts.SALT).getBytes()));
        System.out.println(realUser.getPassword());
        if(!DigestUtils.md5DigestAsHex((user.getPassword()+PublicConsts.SALT).getBytes())
                .equals(realUser.getPassword())){
            throw new BussinessException("用户名或密码不正确");
        }
        User safeUser = new User();
        safeUser.setId(realUser.getId());
        safeUser.setUserAccount(realUser.getUserAccount());
        safeUser.setNickname(realUser.getNickname());
        safeUser.setIsValid(realUser.getIsValid());
        req.getSession().setAttribute("user",safeUser);
        return true;
    }

    @Override
    public boolean doLogout(HttpServletRequest req) {
        User user = (User)req.getSession().getAttribute("user");
        if(user == null)    throw new BussinessException("用户已登出");
        req.getSession().setAttribute("user",null);
        return true;
    }
}




