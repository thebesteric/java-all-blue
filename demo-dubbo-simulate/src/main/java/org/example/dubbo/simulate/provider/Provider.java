package org.example.dubbo.simulate.provider;

import org.example.dubbo.simulate.framework.URL;
import org.example.dubbo.simulate.framework.protocol.Protocol;
import org.example.dubbo.simulate.framework.protocol.ProtocolFactory;
import org.example.dubbo.simulate.framework.register.LocalRegister;
import org.example.dubbo.simulate.framework.register.RemoteRegistryCenter;
import org.example.dubbo.simulate.provider.api.HelloService;
import org.example.dubbo.simulate.provider.impl.HelloServiceImpl;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Provider {
    public static void main(String[] args) throws UnknownHostException {

        // 绑定关系
        LocalRegister.registry(HelloService.class.getName(), HelloServiceImpl.class);

        // 往注册中心注册
        InetAddress ip4 = Inet4Address.getLocalHost();

        String hostname = ip4.getHostAddress(); // 拿本机地址
        Integer port = 8080; // 获取用户配置的端口
        URL url = new URL("http", hostname, port);
        RemoteRegistryCenter.registry(HelloService.class.getName(), url);

        // 启动 server
        // HttpServer httpServer = new HttpServer();
        // httpServer.start(hostname, port);
        // Protocol protocol = new DubboProtocol();
        Protocol protocol = ProtocolFactory.getProtocol(System.getProperty("protocol"));
        protocol.startup(url);
    }
}
