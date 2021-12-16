package org.example.ribbon;

import org.example.mall.anno.MyLoadBalanced;
import org.example.ribbon.interceptor.MyLoadBalancerInterceptor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
// 不推荐使用 @RibbonClients 因为 configuration 所指定的配置类，必须不能被 spring 所扫描到
// @RibbonClients(value = {
//         @RibbonClient(name = "mall-account", configuration = RibbonRuleConfig.class)
// })
public class AppRibbon {

    public static void main(String[] args) {
        SpringApplication.run(AppRibbon.class, args);
    }

    @Bean
    // @MyLoadBalanced
    @LoadBalanced
    public RestTemplate restTemplate() {

        // 如果需要使用 service-name 进行调用的话，需要增加 LoadBalancerInterceptor
        // 这个也是 @LoadBalanced 的实现方式
        // RestTemplate restTemplate = new RestTemplate();
        // restTemplate.setInterceptors(Collections.singletonList(new LoadBalancerInterceptor(loadBalancerClient)));
        // return restTemplate;

        return new RestTemplate();
    }

    @Configuration
    public static class RibbonConfiguration {

    }

    // @Configuration
    public static class MyLoadBalancerConfiguration {

        @Autowired(required = false)
        @MyLoadBalanced // 限定注入属性
        public List<RestTemplate> restTemplates = Collections.emptyList();

        @Bean
        public MyLoadBalancerInterceptor myLoadBalancerInterceptor(LoadBalancerClient loadBalancerClient) {
            return new MyLoadBalancerInterceptor(loadBalancerClient);
        }

        // 利用 spring 提供的扩展点，在 bean 实例化完成之后调用
        @Bean
        public SmartInitializingSingleton myLoadBalancedRestTemplateInitializer(MyLoadBalancerInterceptor myLoadBalancerInterceptor) {
            return new SmartInitializingSingleton() {
                @Override
                public void afterSingletonsInstantiated() {
                    for (RestTemplate restTemplate : restTemplates) {
                        restTemplate.setInterceptors(Collections.singletonList(myLoadBalancerInterceptor));
                    }
                }
            };
        }
    }
}
