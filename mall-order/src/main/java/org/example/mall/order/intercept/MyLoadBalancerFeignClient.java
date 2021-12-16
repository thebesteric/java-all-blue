package org.example.mall.order.intercept;

import feign.Request;
import feign.Response;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;

import java.io.IOException;

// @Component("loadBalancerFeignClient")
public class MyLoadBalancerFeignClient {

    private final LoadBalancerFeignClient loadBalancerFeignClient;

    public MyLoadBalancerFeignClient(LoadBalancerFeignClient loadBalancerFeignClient) {
        this.loadBalancerFeignClient = loadBalancerFeignClient;
    }

    public Response execute(Request request, Request.Options options) throws IOException {
        System.out.println("========== start MyLoadBalancerFeignClient ==========");
        Response response = loadBalancerFeignClient.execute(request, options);
        System.out.println("response = " + response.body());
        System.out.println("========== end MyLoadBalancerFeignClient ==========");
        return response;
    }
}
