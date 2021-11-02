package org.example.mall.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.mall.comm.R;
import org.example.mall.order.feign.UserFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private UserFeignService userFeignService;

    private final RestTemplate restTemplateWithoutLoadbalancer = new RestTemplate();

    @GetMapping("/get/{id}")
    public R getUser(@PathVariable String id) {
        // 1、传统调用方式
        // String url = "http://127.0.0.1:8082/user/get/" + id;
        // R data = restTemplateWithoutLoadbalancer.getForObject(url, R.class);

        // 2、Ribbon 调用方式
        // RestTemplate 调用，负载均衡器 mall-user 服务，选择一个去调用
        // RestTemplate 扩展点，ClientHttpRequestInterceptor
        // Ribbon 实现了 LoadBalancerInterceptor，把 mall-user 替换成 127.0.0.1:8082
        // String url = "http://mall-user/user/get/" + id;
        // R data = restTemplate.getForObject(url, R.class);

        // 3、模拟 Ribbon 调用方式
        // String url = getUrl("mall-user") + "/user/get/" + id;
        // R data = restTemplateWithoutLoadbalancer.getForObject(url, R.class);

        // 4、自定义 loadbalancer
        // String url = "http://mall-user/user/get/" + id;
        // R data = restTemplate.getForObject(url, R.class);

        // 4、Feign 调用方式（封装 Ribbon 调用）
        R data = userFeignService.getUser(id);

        // 获取 mall-user 的所有实例
        List<ServiceInstance> instances = discoveryClient.getInstances("mall-user");
        instances.forEach(serviceInstance -> {
            log.info(serviceInstance.getHost() + ":" + serviceInstance.getPort());
        });

        return data;
    }

    private String getUrl(String serviceId) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        if (instances == null || instances.isEmpty()) {
            return null;
        }
        int instanceSize = instances.size();
        int serviceIndex = incrementAndGetModulo(instanceSize);

        return instances.get(serviceIndex).getUri().toString();
    }

    private final AtomicInteger nextIndex = new AtomicInteger(0);

    private int incrementAndGetModulo(int moduloSize) {
        while (true) {
            int current = nextIndex.get();
            int next = (current + 1) % moduloSize;
            if (nextIndex.compareAndSet(current, next) && current < moduloSize) {
                return current;
            }
        }
    }

}
