package com.fxm.customercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fxm.customercenterbackend.common.ErrorCode;
import com.fxm.customercenterbackend.constant.UserConstant;
import com.fxm.customercenterbackend.exception.BussinessException;
import com.fxm.customercenterbackend.mapper.UserMapper;
import com.fxm.customercenterbackend.model.domain.User;
import com.fxm.customercenterbackend.model.request.PageRequest;
import com.fxm.customercenterbackend.model.request.UserLoginRequest;
import com.fxm.customercenterbackend.service.UserService;
import com.fxm.customercenterbackend.util.AlgorithmUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author fansheng
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-11-27 10:17:49
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {


    @Override
    public List<User> searchPartnerByTags(List<String> tags) {

        LambdaQueryChainWrapper<User> wrapper = lambdaQuery();
        tags.forEach(tag ->{
            wrapper.like(User::getTags,tag);
        });
        return wrapper.list();
    }

    public Page<User> searchPartnerPagesByTags(List<String> tags, PageRequest page){
        LambdaQueryChainWrapper<User> wrapper = lambdaQuery();
        tags.forEach(tag ->{
            wrapper.like(User::getTags,tag);
        });
        return wrapper.page(new Page<>(page.getPageNum(),page.getPageSize()));
    }

    @Override
    public User getLoginUser(HttpServletRequest req) {
        User user = (User)req.getSession().getAttribute(UserConstant.SESSION_USER_ATTR);
        if(user == null)    throw new BussinessException(ErrorCode.NOT_LOGIN);
        return user;
    }

    @Override
    public boolean registerService(String userAccount, String password, String nickname) {
        if(userAccount==null || password==null || nickname == null || userAccount.equals("") || password.equals("")
                || nickname.equals("")) throw new BussinessException(ErrorCode.NULL_ERROR);
        Long count = lambdaQuery().eq(User::getUserAccount, userAccount).count();
        if(count > 0){
            throw new BussinessException("用户信息已存在");
        }
        String md5pwd = DigestUtils.md5DigestAsHex((password + UserConstant.PASSWORD_SALT).getBytes());
        User user = new User();
        user.setUserAccount(userAccount);
        user.setPassword(md5pwd);
        user.setNickname(nickname);
        save(user);
        return true;
    }

    @Override
    public boolean doLogin(UserLoginRequest user, HttpServletRequest req) {
        User realUser = lambdaQuery().eq(User::getUserAccount, user.getUserAccount()).one();
        if(realUser == null){
            throw new BussinessException("用户名或密码不正确");
        }
        if(!DigestUtils.md5DigestAsHex((user.getPassword()+ UserConstant.PASSWORD_SALT).getBytes())
                .equals(realUser.getPassword())){
            throw new BussinessException("用户名或密码不正确");
        }
        User safeUser = new User();
        safeUser.setId(realUser.getId());
        safeUser.setUserAccount(realUser.getUserAccount());
        safeUser.setNickname(realUser.getNickname());
        safeUser.setTags(realUser.getTags());
        safeUser.setUserRole(realUser.getUserRole());
        safeUser.setIsDelete(realUser.getIsDelete());
        safeUser.setCreateTime(realUser.getCreateTime());
        safeUser.setDescription(realUser.getDescription());
        safeUser.setUpdateTime(realUser.getUpdateTime());
        safeUser.setIsValid(realUser.getIsValid());
        req.getSession().setAttribute(UserConstant.SESSION_USER_ATTR,safeUser);
        return true;
    }

    @Override
    public boolean doLogout(HttpServletRequest req) {
        User user = (User)req.getSession().getAttribute("user");
        if(user == null)    throw new BussinessException(ErrorCode.NOT_LOGIN);
        req.getSession().removeAttribute(UserConstant.SESSION_USER_ATTR);
        return true;
    }

    @Override
    public List<User> getRecommend(User user,long num) {
        List<User> userList = lambdaQuery().select(User::getId, User::getTags).isNotNull(User::getTags).list();
        String tags = user.getTags();
        Gson gson = new Gson();
        List<String> tagList = gson.fromJson(tags, new TypeToken<List<String>>(){}.getType());
        // 用户列表的下标 => 相似度
        List<Pair<User, Long>> list = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            User getUser = userList.get(i);
            String userTags = getUser.getTags();
            // 无标签或者为当前用户自己
            if (StringUtils.isBlank(userTags) || Objects.equals(getUser.getId(), user.getId())) {
                continue;
            }
            List<String> userTagList = gson.fromJson(userTags, new TypeToken<List<String>>() {}.getType());
            // 计算分数
            long distance = AlgorithmUtils.minDistance(tagList, userTagList);
            list.add(new Pair<>(getUser, distance));
        }
        // 按编辑距离由小到大排序
        List<Pair<User, Long>> topUserPairList = list.stream()
                .sorted((a, b) -> (int) (a.getValue() - b.getValue()))
                .limit(num)
                .toList();

        // 原本顺序的 userId 列表
        List<Long> userIdList = topUserPairList.stream().map(pair -> pair.getKey().getId()).collect(Collectors.toList());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("id", userIdList);
        // 1, 3, 2
        // User1、User2、User3
        // 1 => User1, 2 => User2, 3 => User3
        Map<Long, List<User>> userIdUserListMap = this.list(userQueryWrapper)
                .stream()
                .map(this::getSafetyUser)
                .collect(Collectors.groupingBy(User::getId));
        List<User> finalUserList = new ArrayList<>();
        for (Long userId : userIdList) {
            finalUserList.add(userIdUserListMap.get(userId).get(0));
        }
        return finalUserList;
    }

    @Override
    public User getSafetyUser(User user) {
        if (user == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setNickname(user.getNickname());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setTags(user.getTags());
        return safetyUser;
    }
}




