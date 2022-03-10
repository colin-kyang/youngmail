package com.example.gateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class youngmallCorsConfiguration {
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        //请求方法 *支持所有方法
        config.addAllowedMethod("*");
        //跨域处理 *允许所有的域
        //springboot 4.0 之后，不允许addAllowedOriginPattern 设置为*
        config.addAllowedOriginPattern("*");
        //请求头 *支持所有
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //匹配所有请求
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}