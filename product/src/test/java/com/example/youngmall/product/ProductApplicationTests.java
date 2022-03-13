package com.example.youngmall.product;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.example.youngmall.product.entity.BrandEntity;
import com.example.youngmall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProductApplicationTests {

//    @Autowired
//    BrandService brandService;


//    @Test
//    void contextLoads() {
//        BrandEntity brandEntity=new BrandEntity();
//        brandEntity.setName("华为");
//        brandService.save(brandEntity);
//        System.out.println("保存成功");
//    }

    //原生sdk 上传文件
    @Test
    public void testUpload() throws FileNotFoundException {
        //新建OSSClient：不要用主账号登陆
        String endpoint="oss-cn-beijing.aliyuncs.com";
        String accessKeyId="LTAI5tGa8sPY7Y9JEHHbxfQ9";
        String accessKeySecret="JR0Pdmh9gr9al3kEvzC7mOmZLddYIN";
        //创建OSSClient 实例
        OSS ossClient=new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
        //上传文件流
        InputStream inputStream =new FileInputStream("/Users/yangke/Pictures/Genji.jpeg");
        ossClient.putObject("kyara-yangmall","用户名/Genji.jpeg",inputStream);
        //关闭OSSClient
        ossClient.shutdown();
        System.out.println("上传完成");
    }

}
