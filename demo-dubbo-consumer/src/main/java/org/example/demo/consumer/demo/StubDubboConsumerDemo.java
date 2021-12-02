package org.example.demo.consumer.demo;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.demo.consumer.AppDubboConsumer;
import org.example.dubbo.iface.DemoService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 本地存根
 */
// @Configuration
public class StubDubboConsumerDemo {

    /**
     * stub：本地存根
     *
     * stub = "true": 表示调用个默认等存根类，规则: 包名+接口名+Stub
     * stub = "xx.xx.XxxStub": 具体指定使用哪一个 stub，该类必须实现 DemoService 接口
     */
    @DubboReference(version = "timeout", timeout = 3000, stub = "true")
    // @DubboReference(version = "timeout", timeout = 3000, stub = "org.example.demo.consumer.demo.stub.DemoServiceCustomStub")
    private DemoService demoService;

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(AppDubboConsumer.class, args);

        DemoService demoService = context.getBean(DemoService.class);

        System.out.println(demoService.sayHello("eric"));

    }
}
