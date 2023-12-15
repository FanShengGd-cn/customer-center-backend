package com.fxm.customercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fxm.customercenterbackend.common.ErrorCode;
import com.fxm.customercenterbackend.exception.BussinessException;
import com.fxm.customercenterbackend.mapper.TeamMapper;
import com.fxm.customercenterbackend.model.domain.Team;
import com.fxm.customercenterbackend.model.domain.User;
import com.fxm.customercenterbackend.model.domain.UserTeam;
import com.fxm.customercenterbackend.model.dto.TeamQuery;
import com.fxm.customercenterbackend.model.request.TeamUpdateRequest;
import com.fxm.customercenterbackend.model.vo.TeamVO;
import com.fxm.customercenterbackend.model.vo.UserVO;
import com.fxm.customercenterbackend.service.TeamService;
import com.fxm.customercenterbackend.service.UserService;
import com.fxm.customercenterbackend.service.UserTeamService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author fansheng
 * @description 针对表【team(队伍)】的数据库操作Service实现
 * @createDate 2023-12-09 10:58:13
 */


@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {

    @Resource
    private TeamMapper teamMapper;
    @Resource
    private UserService userService;

    @Resource
    private UserTeamService userTeamService;



    /**
     * 保存新队伍
     */
    @Transactional
    @Override
    public Boolean saveNewTeam(Team team, HttpServletRequest req) {
        User loginUser = userService.getLoginUser(req);
        if (!loginUser.getId().equals(team.getUserId()) && loginUser.getUserRole() != 0) {
            throw new BussinessException(ErrorCode.NO_AUTH);
        }
        if (StringUtils.isAnyBlank(team.getName()) || team.getExpireTime() == null || team.getMaxNum() == null) {
            throw new BussinessException(ErrorCode.NULL_ERROR);
        }
        if (team.getExpireTime().before(new Date())) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        if (team.getMaxNum() < 1 || team.getMaxNum() > 20) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        if (team.getStatus() == 2 && StringUtils.isAnyBlank(team.getPassword())) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        if (teamMapper.selectTeamNumsByUserId(team.getUserId()) > 5) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean teamId = save(team);
        if (!teamId) {
            throw new BussinessException(ErrorCode.SYSTEM_ERROR);
        }
        UserTeam userTeam = new UserTeam();
        userTeam.setTeamId(team.getId());
        userTeam.setUserId(team.getUserId());
        userTeam.setJoinTime(team.getCreateTime());
        if (!userTeamService.save(userTeam)) {
            throw new BussinessException(ErrorCode.SYSTEM_ERROR);
        }
        return true;
    }

    @Override
    public Boolean updateTeam(TeamUpdateRequest team, HttpServletRequest req) {
        User loginUser = userService.getLoginUser(req);
        if (loginUser == null) {
            throw new BussinessException(ErrorCode.NO_AUTH);
        }
        if (team == null) {
            throw new BussinessException(ErrorCode.NULL_ERROR);
        }
        Team oldTeam = getById(team.getId());
        if (oldTeam == null) {
            throw new BussinessException(ErrorCode.NULL_ERROR);
        }
        if (!loginUser.getId().equals(oldTeam.getUserId()) && !loginUser.getUserRole().equals(0)) {
            throw new BussinessException(ErrorCode.NO_AUTH);
        }

        if (userTeamService.selectUserByTeamId(oldTeam.getId()).size() > team.getMaxNum()) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        if (team.getStatus() == 2 && team.getPassword() == null) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        if (team.getStatus() != 2 && team.getPassword() != null && !team.getPassword().equals("")) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        if (team.getExpireTime().before(new Date())) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        Team updateTeam = new Team();
        BeanUtils.copyProperties(team, updateTeam);
        return lambdaUpdate().eq(Team::getId, updateTeam.getId()).update(updateTeam);
    }

    @Override
    public IPage<TeamVO> selectTeam(TeamQuery teamQuery, HttpServletRequest req) {
        User loginUser = userService.getLoginUser(req);
        LambdaQueryChainWrapper<Team> qw = lambdaQuery();
        if (teamQuery == null) {
            throw new BussinessException(ErrorCode.NULL_ERROR);
        }
        if (loginUser == null) {
            throw new BussinessException(ErrorCode.NO_AUTH);
        }
        if (teamQuery.getPageNum() < 1 && teamQuery.getPageSize() < 1) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        if (teamQuery.getPageNum() >3000 || teamQuery.getPageSize() > 20) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR,"参数超出范围");
        }
        if (teamQuery.getSearchText() != null) {
            qw.and(q ->
                    q.like(Team::getName, teamQuery.getSearchText())
                            .or()
                            .like(Team::getDescription, teamQuery.getSearchText()));
        }
        if(teamQuery.getMaxNum() != null){
            qw.gt(Team::getMaxNum,teamQuery.getMaxNum());
        }
        if(teamQuery.getUserId() != null){
            if(loginUser.getId().equals(teamQuery.getUserId()) ){
                qw.eq(Team::getUserId,teamQuery.getUserId());
            }else{
                qw.and(q->q.eq(Team::getUserId,teamQuery.getUserId()).eq(Team::getStatus,0));
            }
        }
        if(teamQuery.getExpireTime() != null && teamQuery.getExpireTime().after(new Date())){
            qw.gt(Team::getExpireTime,teamQuery.getExpireTime());
        }else{
            qw.gt(Team::getExpireTime,new Date());
        }
        Page<Team> page = qw.page(new Page<>(teamQuery.getPageNum(), teamQuery.getPageSize()));
        return page.convert(t -> {
            TeamVO teamVO = new TeamVO();
            BeanUtils.copyProperties(t, teamVO);
            User user = userService.lambdaQuery().eq(User::getId, teamQuery.getUserId()).one();
            if(user == null)    throw new BussinessException(ErrorCode.SYSTEM_ERROR,"队伍创建者不存在");
            UserVO safeUser = new UserVO();
            BeanUtils.copyProperties(user, safeUser);
            teamVO.setUser(safeUser);
            return teamVO;
        });
    }

    /**
     * 删除队伍
     * @param teamId
     * @param req
     * @return
     */
    @Transactional
    @Override
    public Boolean deleteTeam(Long teamId, HttpServletRequest req) {
        User loginUser = userService.getLoginUser(req);
        if (loginUser == null) {
            throw new BussinessException(ErrorCode.NOT_LOGIN);
        }
        Team team = this.lambdaQuery().eq(Team::getId, teamId).one();
        if(team ==null){
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        if(!team.getUserId().equals(loginUser.getId()) || loginUser.getUserRole()!=0){
            throw new BussinessException(ErrorCode.NO_AUTH);
        }
        List<User> users = userTeamService.selectUserByTeamId(teamId);
        if (users.size() == 0) {
            throw new BussinessException(ErrorCode.SYSTEM_ERROR,"队伍为空");
        }
        if (users.size() > 1){
            throw new BussinessException(ErrorCode.SYSTEM_ERROR,"队伍人数不为1，无法删除");
        }
        Team realTeam = this.getById(teamId);
        User user = users.get(0);
        if (!user.getId().equals(realTeam.getUserId()) && user.getUserRole() != 0){
            throw new BussinessException(ErrorCode.NO_AUTH);
        }
        boolean teamRes = this.lambdaUpdate().eq(Team::getId,teamId).remove();
        boolean userTeamRes = userTeamService.lambdaUpdate().eq(UserTeam::getTeamId, teamId).remove();
        return teamRes&&userTeamRes;
    }


}




