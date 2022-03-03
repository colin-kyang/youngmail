package com.example.youngmall.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.example.youngmall.member.dao")
@EnableFeignClients(basePackages = "com.example.youngmall")
@EnableSwagger2
public class MemeberApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemeberApplication.class,args);
    }
}
