package org.example.demo.consumer.demo;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.demo.consumer.AppDubboConsumer;
import org.example.dubbo.iface.DemoService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * 泛化服务
 */
@Configuration
public class GenericServiceDubboConsumerDemo {

    /**
     * 泛化服务：服务提供者提供到其实是 GenericService
     */
    @DubboReference(version = "generic")
    private DemoService demoService;

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(AppDubboConsumer.class, args);

        DemoService demoService = context.getBean(DemoService.class);
        System.out.println(demoService.sayHello("eric"));
    }
}
