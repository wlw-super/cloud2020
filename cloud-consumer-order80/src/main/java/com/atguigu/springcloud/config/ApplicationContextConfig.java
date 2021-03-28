package com.atguigu.springcloud.config;


import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationContextConfig {

    @Bean
    /**
     * 开启负载均衡策略，默认为轮询
     */
    //@LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
