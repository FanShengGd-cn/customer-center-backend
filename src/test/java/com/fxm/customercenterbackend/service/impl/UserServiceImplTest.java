package com.fxm.customercenterbackend.service.impl;

import com.fxm.customercenterbackend.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class UserServiceImplTest {

    @Resource
    private UserService userService;
    @Test
    void findPartnerByTags() {
        List<String> list = List.of("java");


    }
}