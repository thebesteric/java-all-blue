package org.example.demo.consumer.demo;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.demo.consumer.AppDubboConsumer;
import org.example.dubbo.iface.DemoService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 集群容错
 */
// @Configuration
public class ClusterDubboConsumerDemo {

    /**
     * cluster：容错机制
     *
     * failover: 尝试调用其他的服务
     * failfast: 一次失败就不在尝试调用其他服务了
     * failsafe: 失败后，直接忽略，通常用于写入审计日志
     * failback: 失败自动恢复，后台记录失败的请求，定时重发，通常用于消息通知操作
     * forking: 同时调用其他所有服务，只要有一个正常返回，就表示成功，通常用于对实时性要求较高对场景
     * broadcast: 同时调用其他所有服务，必须所有的都正常返回，就表示成功，通常用于通知服务提供者更新缓存或资源
     */
    @DubboReference(version = "timeout", timeout = 3000, cluster = "failfast")
    private DemoService demoService;

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(AppDubboConsumer.class, args);

        DemoService demoService = context.getBean(DemoService.class);

        System.out.println(demoService.sayHello("eric"));

    }
}
