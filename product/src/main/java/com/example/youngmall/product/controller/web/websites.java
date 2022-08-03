package com.example.youngmall.product.controller.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.rmi.server.UID;
import java.util.UUID;

@Controller
@RequestMapping("/web")
@Slf4j
public class websites {

    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping("/test")
    public String test(){
        log.info("test");
        return "index";
    }

    @GetMapping("/redis")
    @ResponseBody
    public String testRedis() {
        ValueOperations<String,String> ops = redisTemplate.opsForValue();
        // 保存
        ops.set("hello","word_" + UUID.randomUUID().toString());
        // 插叙
        String result = ops.get("hello");
        System.out.println("保存的数据是：" + result);
        return result;
    }

}
