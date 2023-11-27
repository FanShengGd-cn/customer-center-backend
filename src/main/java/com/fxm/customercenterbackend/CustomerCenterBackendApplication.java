package com.fxm.customercenterbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.fxm.customercenterbackend.mapper")
public class CustomerCenterBackendApplication {


    public static void main(String[] args) {
        SpringApplication.run(CustomerCenterBackendApplication.class, args);
    }

}
