package com.fxm.customercenterbackend.service.impl;

import com.fxm.customercenterbackend.model.domain.Team;
import com.fxm.customercenterbackend.service.TeamService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SpringBootTest
class TeamServiceImplTest {

    @Resource
    private TeamService teamService;
    @Test
    void saveNewTeamTest() {

        List<CompletableFuture<Void>> list = new ArrayList<>();
        for(int i = 0; i < 5 ;i++){
            Team team = new Team();
            team.setName("test12345789");
            team.setDescription("test description");
            team.setExpireTime(new Date(System.currentTimeMillis()+12312312L));
            team.setMaxNum(6);
            team.setUserId(1L);
            team.setStatus(0);
            list.add(CompletableFuture.runAsync(() -> {
                System.out.println(Thread.currentThread().getName());
//                System.out.println(teamService.saveNewTeam(team));
            }));
        }
        CompletableFuture.allOf(list.toArray(CompletableFuture[]::new)).join();
    }
}