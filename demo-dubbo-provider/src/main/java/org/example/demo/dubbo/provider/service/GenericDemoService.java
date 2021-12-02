package org.example.demo.dubbo.provider.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.service.GenericException;
import org.apache.dubbo.rpc.service.GenericService;

@DubboService(version = "generic", interfaceName = "org.example.dubbo.iface.DemoService", protocol = {"p1", "p2"})
public class GenericDemoService implements GenericService {
    @Override
    public Object $invoke(String method, String[] parameterTypes, Object[] args) throws GenericException {
        System.out.println("执行了 generic 服务");
        return "执行到方法是 " + method;
    }
}
