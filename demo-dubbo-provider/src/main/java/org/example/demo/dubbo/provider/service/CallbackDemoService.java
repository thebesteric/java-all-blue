package org.example.demo.dubbo.provider.service;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.Argument;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.rpc.RpcContext;
import org.example.dubbo.iface.DemoService;
import org.example.dubbo.iface.DemoServiceListener;

@DubboService(version = "callback",
        // 指定回调方法和参数
        methods = {@Method(name = "sayHello", arguments = {@Argument(index = 2, callback = true)})},
        // 最大支持几个回调
        callbacks = 3, protocol = {"p1", "p2"})
public class CallbackDemoService implements DemoService {

    @Override
    public String sayHello(String name) {
        System.out.println(CallbackDemoService.class.getSimpleName() + " 执行了回调任务: " + name);
        URL url = RpcContext.getContext().getUrl();
        return String.format("%s, %s, Hello %s", url.getProtocol(), url.getPort(), name);
    }

    @Override
    public String sayHello(String name, String key, DemoServiceListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                listener.changed(key);
            }
        }).start();

        return sayHello(name);
    }
}
