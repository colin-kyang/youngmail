package com.example.youngmall.product.controller.web;

import com.example.youngmall.product.entity.CategoryEntity;
import com.example.youngmall.product.entity.vo.Catalog2Vo;
import com.example.youngmall.product.service.CategoryService;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;

@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        // >> TODO: 1.查出所有的1级分类
        List<CategoryEntity> categoryEntityList = categoryService.getLevel1Category();
        model.addAttribute("categoryList", categoryEntityList);
        return "index";
    }

    @GetMapping("/index/catelog.json")
    @ResponseBody
    public Map<String, List<Catalog2Vo>> getCatalogJson() throws InterruptedException {
        return categoryService.getCatelogJsonFromRedisWithRedisLock();
    }

    @GetMapping("/hello")
    @ResponseBody
    public String getHelloTest() {
        //1. 获取一把锁，只要锁的名字相同，就是同一把锁
        RLock lock = redissonClient.getLock("my-lock");
        lock.lock();
        try {
            System.out.println("加锁成功，执行业务");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 无论如何都需要进行解锁操作
            // 解锁代码没有运行，redis会不会出现死锁？
            // 即使中间发生错误，也会自动释放锁
            System.out.println("释放锁..." + Thread.currentThread().getId());
            lock.unlock();
        }
        return "Hello";
    }

    @GetMapping("/write")
    @ResponseBody
    public String writeValue() throws InterruptedException {
        ReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
        String s = "";
        RLock rLock = (RLock) lock.writeLock();
        try {
            // 1. 写数据设置写锁，读数据设置读锁
            rLock.lock();
            s = UUID.randomUUID().toString();
            Thread.sleep(30000);
            redisTemplate.opsForValue().set("writeValue", s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return s;
    }

    @GetMapping("/read")
    @ResponseBody
    public String readValue() {
        String s = "";
        ReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
        RLock rLock = (RLock) lock.readLock();
        //加读锁
        rLock.lock();
        try {
            s = redisTemplate.opsForValue().get("writeValue");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return s;
    }
    @GetMapping("/park")
    @ResponseBody
    public String park() throws InterruptedException {
        RSemaphore park = redissonClient.getSemaphore("park");
        park.acquire();// 获取一个信号，获取一个值，占用一个车位
        return "ok";
    }

    @GetMapping("/go")
    @ResponseBody
    public String go() {
        RSemaphore park = redissonClient.getSemaphore("park");
        park.release();// 释放一个车位
        return "ok";
    }

    @GetMapping("/lockDoor")
    @ResponseBody
    public String lockDoor() throws InterruptedException {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.trySetCount(5);
        door.await();// 等待闭锁完成
        return "放假了...";
    }

    @GetMapping("/gogo/{id}")
    @ResponseBody
    public String gogo(@PathVariable Long id) {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.countDown();//计数减1
        return id + "放假了";
    }


}
