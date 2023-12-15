package com.fxm.customercenterbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.fxm.customercenterbackend.common.ErrorCode;
import com.fxm.customercenterbackend.constant.UserConstant;
import com.fxm.customercenterbackend.exception.BussinessException;
import com.fxm.customercenterbackend.mapper.UserTeamMapper;
import com.fxm.customercenterbackend.model.domain.Team;
import com.fxm.customercenterbackend.model.domain.User;
import com.fxm.customercenterbackend.model.domain.UserTeam;
import com.fxm.customercenterbackend.service.UserService;
import com.fxm.customercenterbackend.service.UserTeamService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author fansheng
 * @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
 * @createDate 2023-12-11 17:36:41
 */
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
        implements UserTeamService {
    @Resource
    private UserTeamMapper userTeamMapper;

    @Resource
    private UserService userService;

    @Resource
    private RedissonClient redissonClient;



    private Team checkTeamExist(Long teamId) {
        if (teamId <= 0) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = Db.lambdaQuery(Team.class).eq(Team::getId, teamId).one();
        if (team == null) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        return team;
    }

    @Override
    public List<User> selectUserByTeamId(Long teamId) {
        return userTeamMapper.selectUserByTeamId(teamId);
    }

    @Override
    public Boolean quitTeam(Long teamId, HttpServletRequest req) {
        Team team = checkTeamExist(teamId);
        User loginUser = userService.getLoginUser(req);
        if (loginUser.getId().equals(team.getUserId())) {
            throw new BussinessException(ErrorCode.SYSTEM_ERROR, "创建者不能退出队伍");
        }
        UserTeam userTeam = this.lambdaQuery().eq(UserTeam::getUserId, loginUser.getId()).eq(UserTeam::getTeamId, team.getId()).one();
        boolean remove;
        if (userTeam == null) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        } else {
            remove = this.lambdaUpdate().eq(UserTeam::getId, userTeam.getId()).remove();
        }
        return remove;
    }

    @Override
    public Long joinTeam(Long teamId, HttpServletRequest req) {
        Team team = checkTeamExist(teamId);
        User loginUser = userService.getLoginUser(req);
        if (loginUser.getId().equals(team.getUserId())) {
            throw new BussinessException(ErrorCode.SYSTEM_ERROR, "创建者不能加入自己创建的队伍");
        }
        List<User> users = userTeamMapper.selectUserByTeamId(teamId);
        if (users.size() >= team.getMaxNum()) {
            throw new BussinessException(ErrorCode.SYSTEM_ERROR, "队伍人数已满");
        }
        boolean alreadyTeamMember = users.stream().anyMatch(u -> u.getId().equals(loginUser.getId()));
        if (alreadyTeamMember) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR, "已经是队伍成员");
        }
        RLock lock = redissonClient.getLock(UserConstant.USER_JOIN_TEAM_LOCK);
        try{
            while (true) {
                if (lock.tryLock(0,-1, TimeUnit.MILLISECONDS)){
                    System.out.println("getLock: " + Thread.currentThread().getId());
                    UserTeam userTeam = new UserTeam();
                    userTeam.setUserId(loginUser.getId());
                    userTeam.setTeamId(teamId);
                    userTeam.setJoinTime(new Date());
                    boolean save = save(userTeam);
                    Thread.sleep(5000);
                    if (!save) {
                        throw new BussinessException(ErrorCode.SYSTEM_ERROR, "加入队伍失败");
                    }
                    return userTeam.getId();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()){
                System.out.println("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }

    }
}




