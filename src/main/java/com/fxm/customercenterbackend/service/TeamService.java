package com.fxm.customercenterbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fxm.customercenterbackend.model.domain.Team;
import com.fxm.customercenterbackend.model.dto.TeamQuery;
import com.fxm.customercenterbackend.model.request.TeamUpdateRequest;
import com.fxm.customercenterbackend.model.vo.TeamVO;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author xiang
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2023-12-09 10:58:13
*/
public interface TeamService extends IService<Team> {

    Boolean saveNewTeam(Team team, HttpServletRequest req);


    Boolean updateTeam(TeamUpdateRequest team, HttpServletRequest req);

    IPage<TeamVO> selectTeam(TeamQuery teamQuery, HttpServletRequest req);

    Boolean deleteTeam(Long teamId, HttpServletRequest req);
}
