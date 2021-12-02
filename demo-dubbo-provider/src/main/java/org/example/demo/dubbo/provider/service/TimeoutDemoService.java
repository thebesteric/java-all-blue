package org.example.demo.dubbo.provider.service;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.example.dubbo.iface.DemoService;

import java.util.concurrent.TimeUnit;

/**
 * timeout：表示服务端最大等待时间
 * 如果消费端没有配置 timeout 则表示总超时时间以服务端为准
 * 如果消费端配置了 timeout 这各自遵守各自的超时时间
 */
@DubboService(version = "timeout", timeout = 6000, protocol = {"p1", "p2"})
public class TimeoutDemoService implements DemoService {

    @Override
    public String sayHello(String name) {
        System.out.println(TimeoutDemoService.class.getSimpleName() + " => 执行了任务: " + name);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(TimeoutDemoService.class.getSimpleName() + " <= 执行结束了: " + name);

        URL url = RpcContext.getContext().getUrl();
        return String.format("%s, %s, Hello %s", url.getProtocol(), url.getPort(), name);
    }
}
