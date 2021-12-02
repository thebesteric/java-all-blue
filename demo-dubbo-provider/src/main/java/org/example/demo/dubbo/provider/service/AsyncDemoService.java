package org.example.demo.dubbo.provider.service;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.example.dubbo.iface.DemoService;

import java.util.concurrent.CompletableFuture;

@DubboService(version = "async", protocol = {"p1", "p2"})
public class AsyncDemoService implements DemoService {
    @Override
    public String sayHello(String name) {
        URL url = RpcContext.getContext().getUrl();
        return String.format("%s, %s, Hello %s", url.getProtocol(), url.getPort(), name);
    }

    @Override
    public CompletableFuture<String> sayHelloAsync(String name) {
        System.out.println(AsyncDemoService.class.getSimpleName() + " 执行了异步任务: " + name);
        URL url = RpcContext.getContext().getUrl();
        return CompletableFuture.supplyAsync(() -> {
            return String.format("%s, %s, Hello %s", url.getProtocol(), url.getPort(), name);
        });
    }
}
