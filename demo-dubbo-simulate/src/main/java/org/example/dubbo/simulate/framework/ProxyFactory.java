package org.example.dubbo.simulate.framework;

import org.example.dubbo.simulate.framework.protocol.ProtocolFactory;
import org.example.dubbo.simulate.framework.register.RemoteRegistryCenter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

public class ProxyFactory {
    @SuppressWarnings("unchecked")
    public static <T> T getProxy(final Class<?> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // -Dmock=test
                String mock = System.getProperty("mock");
                if (mock != null) {
                    return mock;
                }
                try {
                    // 构建对象
                    Invocation invocation = new Invocation(interfaceClass.getName(), method.getName(),
                            method.getParameterTypes(), args);
                    // 从注册中心获取地址
                    Set<URL> urls = RemoteRegistryCenter.get(interfaceClass.getName());
                    // 负载均衡策略
                    URL url = LoadBalance.random(urls);

                    // HttpClient httpClient = new HttpClient();
                    // return httpClient.send(url.getProtocol() + "://" + url.getHostname(), url.getPort(), invocation);

                    // return new HttpProtocol().send(url, invocation);
                    // return new DubboProtocol().send(url, invocation);

                    return ProtocolFactory.getProtocol(System.getProperty("protocol")).send(url, invocation);

                } catch (Exception ex) {
                    return "服务调用失败";
                }
            }
        });
    }
}
