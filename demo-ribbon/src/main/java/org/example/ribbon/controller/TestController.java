package org.example.ribbon.controller;

import org.example.mall.comm.R;
import org.example.ribbon.service.AccountFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class TestController {

    @Autowired
    private AccountFeignService accountFeignService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/get/{id}")
    public R getAccount(@PathVariable String id) {

        // 模拟 ribbon
        // String url = getUrl("mall-account") + "/account/" + id;
        // return restTemplate.getForObject(url, R.class);

        String url = "http://mall-account/account/" + id;
        return restTemplate.getForObject(url, R.class);

        // 使用 feign
        // return accountFeignService.get(id);
    }


    @Autowired
    private DiscoveryClient discoveryClient;

    public String getUrl(String serviceName) {
        // 获取服务实例
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        if (instances == null || instances.isEmpty()) {
            return null;
        }
        int size = instances.size();
        // 轮询算法
        int currentIndex = incrementAndGetModulo(size);
        return instances.get(currentIndex).getUri().toString();

    }

    private final AtomicInteger nextIndex = new AtomicInteger(0);

    private int incrementAndGetModulo(int modulo) {
        for(;;) {
            int current = this.nextIndex.get();
            int next = (current + 1) % modulo;
            if (nextIndex.compareAndSet(current, next) && current < modulo) {
                return current;
            }
        }
    }

}
