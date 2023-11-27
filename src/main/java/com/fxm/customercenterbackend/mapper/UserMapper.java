package com.fxm.customercenterbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fxm.customercenterbackend.domain.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper extends BaseMapper<User> {
}
