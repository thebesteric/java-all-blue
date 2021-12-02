package org.example.demo.consumer.demo;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.demo.consumer.AppDubboConsumer;
import org.example.dubbo.iface.DemoService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * 负载均衡
 */
// @Configuration
public class LoadBalanceDubboConsumerDemo {

    /**
     * 在集群负载均衡时，Dubbo 提供了多种均衡策略，缺省为 random 随机调用
     *
     * random: 随机调用
     * roundrobin: 轮询
     * consistenthash: 一致性哈希
     * leastactive: 最少活跃调用数
     */
    @DubboReference(version = "default", loadbalance = "roundrobin")
    private DemoService demoService;

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(AppDubboConsumer.class, args);

        DemoService demoService = context.getBean(DemoService.class);

        for (int i = 1; i <= 1000; i++) {
            System.out.println(i + " = " + demoService.sayHello(i%5 + "-eric"));
            TimeUnit.SECONDS.sleep(1);
        }

    }
}
