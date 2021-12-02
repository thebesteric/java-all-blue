package org.example.dubbo.simulate.consumer;

import org.example.dubbo.simulate.framework.ProxyFactory;
import org.example.dubbo.simulate.provider.api.HelloService;

import java.net.UnknownHostException;

public class Consumer {
    public static void main(String[] args) throws UnknownHostException {
        // HttpClient httpClient = new HttpClient();
        // Invocation invocation = new Invocation(HelloService.class.getName(), "sayHello",
        //         new Class[]{String.class}, new Object[]{"eric"});
        // String result = httpClient.send("http", "localhost", 8080, invocation);
        // System.out.println(result);

        HelloService helloService = ProxyFactory.getProxy(HelloService.class);
        String result = helloService.sayHello("eric");
        System.out.println(result);
    }
}
