package com.example.third_party.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example.third_party")
/**
 * 原生上传工具
 */
public class UploadConfig {
    @Value("${spring.cloud.alicloud.access-key}")
    String accessKeyId;

    @Value("${spring.cloud.alicloud.accessKeySecret}")
    String accessKeySecret;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    String endpoint;


    @Bean
    public OSS OSSClient()
    {
        return  new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
    }
}
