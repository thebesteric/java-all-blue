package org.example.demo.consumer.demo;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.service.GenericService;
import org.example.demo.consumer.AppDubboConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 泛化调用
 */
// @Configuration
public class GenericDubboConsumerDemo {

    /**
     * 泛化调用：不依赖接口
     */
    @DubboReference(version = "default",
            id = "demoService",
            interfaceName = "org.example.dubbo.iface.DemoService",
            generic = true)
    private GenericService genericService;

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(AppDubboConsumer.class, args);

        GenericService genericService = (GenericService) context.getBean("demoService");

        Object result = genericService.$invoke("sayHello", new String[]{String.class.getName()}, new Object[]{"eric"});
        System.out.println(result);
    }
}
