package org.example.demo.consumer.demo;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.demo.consumer.AppDubboConsumer;
import org.example.dubbo.iface.DemoService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.CompletableFuture;

/**
 * 异步调用
 */
// @Configuration
public class AsyncDubboConsumerDemo {

    /**
     * 异步调用
     */
    @DubboReference(version = "async")
    private DemoService demoService;

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(AppDubboConsumer.class, args);

        DemoService demoService = context.getBean(DemoService.class);

        CompletableFuture<String> completableFuture = demoService.sayHelloAsync("eric");
        completableFuture.whenComplete((v, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            } else {
                System.out.println(v);
            }
        });

        System.out.println("异步调用结束");
    }
}
