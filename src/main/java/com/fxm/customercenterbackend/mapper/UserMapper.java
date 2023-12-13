package com.fxm.customercenterbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fxm.customercenterbackend.model.domain.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper extends BaseMapper<User> {
}
