package org.example.demo.consumer.demo.stub;

import org.example.dubbo.iface.DemoService;

public class DemoServiceStub implements DemoService {

    private final DemoService demoService;

    // 通过构造函数传入真正当 demoService 的实现类
    public DemoServiceStub(DemoService demoService) {
        this.demoService = demoService;
    }

    @Override
    public String sayHello(String name) {
        try {
            // 此代码会在客户端执行，可以在客户端做 ThreadLocal 本地缓存，或者验证参数合法性等
            return demoService.sayHello(name);
        } catch (Exception ex) {
            // 容错数据
            return "容错数据";
        }
    }
}
