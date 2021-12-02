package org.example.demo.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableDiscoveryClient
public class AppDubboConsumer {

    // 使用 @DubboReference 将 DemoService 加入 Spring Context，然后才能 getBean
    // @DubboReference 两个作用：1、引入 dubbo service；2、加入 Spring Context
    // @DubboReference(version = "default")
    // private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AppDubboConsumer.class, args);

        // DemoService demoService = context.getBean(DemoService.class);
        // System.out.println(demoService.sayHello("eric"));
    }
}
