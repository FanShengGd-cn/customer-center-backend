package com.fxm.customercenterbackend;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fxm.customercenterbackend.constant.UserConstant;
import com.fxm.customercenterbackend.mapper.UserMapper;
import com.fxm.customercenterbackend.model.domain.User;
import com.fxm.customercenterbackend.model.request.PageRequest;
import com.fxm.customercenterbackend.service.UserService;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
        Page<User> page = userService.searchPartnerPagesByTags(List.of("female"),new PageRequest(1,10));
//        System.out.println(userList);
        System.out.println(page.getRecords());
//        Assertions.assertFalse(StringUtils.isAnyBlank(user.getUserAccount(), user.getNickname()));
//        System.out.println(userMapper.insert(user));
    }
    @Test
    public void testUserService() {
        boolean update = userService.lambdaUpdate().eq(User::getUserAccount, "fansheng").set(User::getIsValid, 1).update();
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        int j = 0;
        long start = System.currentTimeMillis();
        for(int i = 0 ; i < 20 ; i++){
            List<User> userList = new ArrayList<>();
            do {
                j++;
                User user = new User();
                user.setUserAccount(String.format("fakeFansheng-%d-%d",i,j));
                user.setNickname("假用户");
                user.setTags("['java','spring','python','male']");
                user.setPassword(DigestUtils.md5DigestAsHex(("123456"+ UserConstant.PASSWORD_SALT).getBytes()));
                userList.add(user);
            } while (j % 50000 != 0);
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                userService.saveBatch(userList, 50000);
            });
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
        long end = System.currentTimeMillis()-start;
        System.out.println(end);
        System.out.println(update);


    }

    @Test
    public void testStringUtils() throws ExecutionException, InterruptedException {
//        Db.lambdaUpdate(User.class).gt(User::getId,10).remove();
//        Callable<String> callable = () -> {
//             System.out.println();
//             return Thread.currentThread().getName();
//        };
//        FutureTask<String> futureTask = new FutureTask<>(callable);
//        new Thread(futureTask).start();
//        String s = futureTask.get();
//        System.out.println(s);
        List<User> list = new ArrayList<>();
        for(int i = 0; i < 10 ; i++){
            User user = new User();
            user.setTags("1231231");
            list.add(user);
        }
        List<String> collect = list.stream().distinct().map(User::getTags).toList();
        System.out.println(collect);
    }

}