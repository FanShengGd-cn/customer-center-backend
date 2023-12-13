package com.fxm.customercenterbackend.mapper;

import com.fxm.customercenterbackend.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class UserTeamMapperTest {
    @Resource
    private UserTeamMapper userTeamMapper;
    @Test
    void testSelectUserByTeamId() {
        List<User> users = userTeamMapper.selectUserByTeamId(1L);
        users.forEach(System.out::println);
    }
}