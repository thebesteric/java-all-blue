package org.example.mall.order.intercept;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequestFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URI;

@Slf4j
public class MyLoadBalancerInterceptor implements ClientHttpRequestInterceptor {

    private final LoadBalancerClient loadBalancerClient;
    private LoadBalancerRequestFactory requestFactory;

    public MyLoadBalancerInterceptor(LoadBalancerClient loadBalancerClient, LoadBalancerRequestFactory requestFactory) {
        this.loadBalancerClient = loadBalancerClient;
        this.requestFactory = requestFactory;
    }

    public MyLoadBalancerInterceptor(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
        this.requestFactory = new LoadBalancerRequestFactory(loadBalancerClient);
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        URI originalUri = httpRequest.getURI();
        String serviceName = originalUri.getHost();
        Assert.state(serviceName != null, "Request URI dose not contain a valid hostname: " + originalUri);
        log.info("进入自定义请求拦截器中 {}", serviceName);
        return this.loadBalancerClient.execute(serviceName, requestFactory.createRequest(httpRequest, body, execution));
    }
}
