package com.fxm.customercenterbackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fxm.customercenterbackend.common.BaseResponse;
import com.fxm.customercenterbackend.common.Constants;
import com.fxm.customercenterbackend.common.ErrorCode;
import com.fxm.customercenterbackend.common.ResultUtil;
import com.fxm.customercenterbackend.exception.BussinessException;
import com.fxm.customercenterbackend.model.domain.Team;
import com.fxm.customercenterbackend.model.domain.User;
import com.fxm.customercenterbackend.model.dto.TeamQuery;
import com.fxm.customercenterbackend.model.request.TeamUpdateRequest;
import com.fxm.customercenterbackend.model.vo.TeamVO;
import com.fxm.customercenterbackend.service.TeamService;
import com.fxm.customercenterbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/team")
public class TeamController {

    @Resource
    private TeamService teamService;

    @Resource
    private UserService userService;

    @PostMapping ("/add")
    public BaseResponse<Long> addTeam(@RequestBody Team team, HttpServletRequest request){
        if(team == null || StringUtils.isAnyBlank(team.getName()))
            throw new BussinessException(ErrorCode.NULL_ERROR);
        Boolean res = teamService.saveNewTeam(team,request);
        if(!res){
            throw new BussinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtil.success(team.getId());
    }


    @PostMapping("/update")
    public BaseResponse<Long> updateTeam(@RequestBody TeamUpdateRequest team, HttpServletRequest req){
        User user = (User) req.getSession().getAttribute(Constants.SESSION_USER_ATTR);
        if(team == null || StringUtils.isAnyBlank(team.getName())){
            throw new BussinessException(ErrorCode.NULL_ERROR);
        }
        if(user == null){
            throw new BussinessException(ErrorCode.NO_AUTH);
        }
        Boolean res = teamService.updateTeam(team,req);
        if(!res){
            throw new BussinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtil.success(team.getId());
    }

    @PostMapping("/query")
    public BaseResponse<IPage<TeamVO>> selectTeam(@RequestBody TeamQuery teamQuery, HttpServletRequest req){
        if(teamQuery == null){
            throw new BussinessException(ErrorCode.NULL_ERROR);
        }
        if (userService.getLoginUser(req) == null){
            throw new BussinessException(ErrorCode.NO_AUTH);
        }
        IPage<TeamVO> teamVOPage = teamService.selectTeam(teamQuery, req);
        return ResultUtil.success(teamVOPage);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestParam Long teamId, HttpServletRequest req){
        if(teamId == null || teamId <= 0){
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtil.success(teamService.deleteTeam(teamId, req));
    }
}
