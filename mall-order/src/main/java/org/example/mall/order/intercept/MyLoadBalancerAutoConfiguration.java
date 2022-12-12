package org.example.mall.order.intercept;

import org.example.mall.anno.MyLoadBalanced;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

// @Configuration
public class MyLoadBalancerAutoConfiguration {

    @Autowired(required = false)
    @MyLoadBalanced // 限定注入属性
    public List<RestTemplate> restTemplates = Collections.emptyList();

    @Bean
    public MyLoadBalancerInterceptor myLoadBalancerInterceptor(LoadBalancerClient loadBalancerClient) {
        return new MyLoadBalancerInterceptor(loadBalancerClient);
    }

    // 为何要定义 SmartInitializingSingleton 呢
    // 因为 SmartInitializingSingleton 是在 spring 完成所有的 bean 初始化之后，才会调用
    @Bean
    public SmartInitializingSingleton myLoadBalancedRestTemplateInitializer(MyLoadBalancerInterceptor myLoadBalancerInterceptor) {
        return () -> {
            for (RestTemplate restTemplate : restTemplates) {
                restTemplate.setInterceptors(Collections.singletonList(myLoadBalancerInterceptor));
            }
        };
    }
}