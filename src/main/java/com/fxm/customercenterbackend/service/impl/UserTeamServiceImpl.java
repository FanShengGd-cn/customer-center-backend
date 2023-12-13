package com.fxm.customercenterbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fxm.customercenterbackend.model.domain.User;
import com.fxm.customercenterbackend.model.domain.UserTeam;
import com.fxm.customercenterbackend.service.UserTeamService;
import com.fxm.customercenterbackend.mapper.UserTeamMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author fansheng
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2023-12-11 17:36:41
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{
    @Resource
    private UserTeamMapper userTeamMapper;

    @Override
    public List<User> selectUserByTeamId(Long teamId) {
        return userTeamMapper.selectUserByTeamId(teamId);
    }
}




