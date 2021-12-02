package org.example.demo.consumer.demo;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.demo.consumer.AppDubboConsumer;
import org.example.demo.consumer.demo.callback.DemoServiceListenerImpl;
import org.example.dubbo.iface.DemoService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 回调函数
 */
// @Configuration
public class CallbackDubboConsumerDemo {

    /**
     * 回调函数
     */
    @DubboReference(version = "callback")
    private DemoService demoService;

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(AppDubboConsumer.class, args);

        DemoService demoService = context.getBean(DemoService.class);

        System.out.println(demoService.sayHello("eric", "k1", new DemoServiceListenerImpl()));
        System.out.println(demoService.sayHello("eric", "k2", new DemoServiceListenerImpl()));
        System.out.println(demoService.sayHello("eric", "k3", new DemoServiceListenerImpl()));

    }
}
