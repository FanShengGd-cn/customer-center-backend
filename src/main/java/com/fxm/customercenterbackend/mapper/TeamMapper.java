package com.fxm.customercenterbackend.mapper;

import com.fxm.customercenterbackend.model.domain.Team;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author xiang
* @description 针对表【team(队伍)】的数据库操作Mapper
* @createDate 2023-12-09 10:58:13
* @Entity generator.domain.Team
*/
public interface TeamMapper extends BaseMapper<Team> {
    Integer selectTeamNumsByUserId(Long userId);
}




