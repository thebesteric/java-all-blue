package org.example.mall.order;

import com.alibaba.cloud.nacos.ribbon.NacosRule;
import com.netflix.loadbalancer.IRule;
import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import io.github.thebesteric.framework.switchlogger.annotation.EnableSwitchLogger;
import org.example.mall.anno.MyLoadBalanced;
import org.example.mall.order.intercept.FeignAuthInterceptor;
import org.example.mall.order.intercept.MyLoadBalancerInterceptor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient // 开启 nacos client（可以不用加）
@EnableFeignClients // 开启 feign client
// 不推荐使用 @RibbonClients 因为 configuration 所指定的配置类，必须不能被 spring 所扫描到
// @RibbonClients(value = {
//         @RibbonClient(name = "mall-user", configuration = RibbonRuleConfig.class)
// })
@EnableSwitchLogger
public class OrderApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(OrderApp.class, args);
        System.out.println(context);
    }

    @Bean
    @LoadBalanced
    // @MyLoadBalanced
    public RestTemplate restTemplate(LoadBalancerClient loadBalancerClient) {
        return new RestTemplate();

        // 如果需要使用 service-name 进行调用的话，需要增加 LoadBalancerInterceptor
        // 这个也是 @LoadBalanced 的实现方式
        // RestTemplate restTemplate = new RestTemplate();
        // restTemplate.setInterceptors(Collections.singletonList(new LoadBalancerInterceptor(loadBalancerClient)));
        // return restTemplate;
    }

    // 这种方式是全局配置，会对所有微服务生效
    // 如果要使用局部配置，请在 yml 文件中配置
    // @Bean
    public IRule ribbonRule() {
        // 指定使用 nacos 提供的负载均衡策略（优先调用同一集群的实例，基于随机权重）
        return new NacosRule();
        // return new VersionRule();
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

        @Bean
        public SmartInitializingSingleton myLoadBalancedRestTemplateInitializer(MyLoadBalancerInterceptor myLoadBalancerInterceptor) {
            return () -> {
                for (RestTemplate restTemplate : restTemplates) {
                    restTemplate.setInterceptors(Collections.singletonList(myLoadBalancerInterceptor));
                }
            };
        }
    }

    // 全局配置，如果单独给某个微服务配置，需要在 @FeignClient 配置 configuration 属性，并注释 @Configuration
    // @FeignClient(value = "mall-user", path = "/user", configuration = OrderApp.FeignConfiguration.class)
    @Configuration // 不推荐，容易配置成全局
    public static class FeignConfiguration {

        @Bean
        public Request.Options options() {
            return new Request.Options(5000, 10000);
        }

        @Bean
        public Logger.Level feignLogLevel() {
            // NONE: 不输出日志
            // BASIC: 只输出请求方法的 URL，响应状态码，接口执行时间
            // HEADERS: BASIC 级别的信息 + 请求头信息
            // FULL: 完整的信息
            return Logger.Level.FULL;
        }

        // Feign 拦截器
        @Bean
        public RequestInterceptor basicAuthRequestInterceptor() {
            // 开始 Basic 认证，会自动在请求头添加 Authorization: Basic ZXJpYzoxMjM=
            // return new BasicAuthRequestInterceptor("eric", "123");

            // 自定义拦截器
            return new FeignAuthInterceptor();
        }
    }


}
