package org.example.demo.dubbo.provider.service;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.example.dubbo.iface.DemoService;

@DubboService(version = "default", protocol = {"p1", "p2"})
public class DefaultDemoService implements DemoService {
    @Override
    public String sayHello(String name) {
        System.out.println(DefaultDemoService.class.getSimpleName() + " 执行了任务: " + name);
        URL url = RpcContext.getContext().getUrl();
        return String.format("%s, %s, Hello %s", url.getProtocol(), url.getPort(), name);
    }
}
