package org.example.demo.consumer.demo;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.demo.consumer.AppDubboConsumer;
import org.example.dubbo.iface.DemoService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 服务降级
 */
// @Configuration
public class MockDubboConsumerDemo {

    /**
     * mock：当调用失败后，返回 mock 数据
     *
     * fail: 失败后返回 mock 数据
     * force: 不会真实调用服务，直接返回 mock 数据
     */
    @DubboReference(version = "timeout", timeout = 3000, mock = "fail: return mock")
    private DemoService demoService;

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(AppDubboConsumer.class, args);

        DemoService demoService = context.getBean(DemoService.class);

        System.out.println(demoService.sayHello("eric"));

    }
}
