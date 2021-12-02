package org.example.demo.consumer.demo;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.demo.consumer.AppDubboConsumer;
import org.example.dubbo.iface.DemoService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 响应超时
 */
// @Configuration
public class TimeoutDubboConsumerDemo {

    /**
     * timeout：表示消费端最大等待时间
     *
     * 如果消费端没有配置 timeout 则表示总超时时间以服务端为准
     * 如果消费端配置了 timeout 这各自遵守各自的超时时间
     */
    @DubboReference(version = "timeout", timeout = 3000)
    private DemoService demoService;

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(AppDubboConsumer.class, args);

        DemoService demoService = context.getBean(DemoService.class);

        System.out.println(demoService.sayHello("eric"));

    }
}
