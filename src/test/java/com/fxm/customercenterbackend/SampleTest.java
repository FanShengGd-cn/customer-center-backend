package com.fxm.customercenterbackend;

import com.fxm.customercenterbackend.domain.User;
import com.fxm.customercenterbackend.mapper.UserMapper;
import com.fxm.customercenterbackend.service.UserService;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SampleTest {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @Test
    public void testUser() {
        User user = new User();
        user.setUserAccount("fansheng");
        user.setNickname("Fansheng");
        user.setPassword("123456");
        System.out.println(userMapper.insert(user));
    }
    @Test
    public void testUserService() {
        boolean update = userService.lambdaUpdate().eq(User::getUserAccount, "fansheng").set(User::getIsValid, 1).update();
        User user = new User();
        user.setUserAccount("fansheng");
        user.setNickname("Fansheng");
        user.setPassword("123456");
        System.out.println(update);
    }

}