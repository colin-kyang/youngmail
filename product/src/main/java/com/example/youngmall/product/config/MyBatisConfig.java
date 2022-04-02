package com.example.youngmall.product.config;


import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Mybatis-Plus 插件设置
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.example.youngmall.product.dao")
public class MyBatisConfig {

    //引入分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor()
    {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        //设置请求页面大于最后页面操作，true 回到首页，false 继续请求，默认false
        paginationInterceptor.setOverflow(false);
        //设置最大单页限制数量，默认500 条，-1 不受限制
        return paginationInterceptor;

    }
}
