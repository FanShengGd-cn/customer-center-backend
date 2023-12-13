package com.fxm.customercenterbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fxm.customercenterbackend.model.domain.User;
import com.fxm.customercenterbackend.model.domain.UserTeam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author xiang
* @description 针对表【user_team(用户队伍关系)】的数据库操作Mapper
* @createDate 2023-12-11 17:36:41
* @Entity generator.domain.UserTeam
*/
@Mapper
public interface UserTeamMapper extends BaseMapper<UserTeam> {
    List<User> selectUserByTeamId(Long teamId);
}




