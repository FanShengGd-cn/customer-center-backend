package com.fxm.customercenterbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fxm.customercenterbackend.model.domain.User;
import com.fxm.customercenterbackend.model.domain.UserTeam;

import java.util.List;

/**
* @author xiang
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service
* @createDate 2023-12-11 17:36:41
*/
public interface UserTeamService extends IService<UserTeam> {
    public List<User> selectUserByTeamId(Long teamId);
}
