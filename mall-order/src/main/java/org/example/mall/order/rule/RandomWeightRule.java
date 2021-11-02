package org.example.mall.order.rule;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class RandomWeightRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    @SuppressWarnings("rawtypes")
    public Server choose(Object key) {
        DynamicServerListLoadBalancer loadBalancer = (DynamicServerListLoadBalancer) this.getLoadBalancer();
        NamingService namingService = this.nacosDiscoveryProperties.namingServiceInstance();
        String serviceName = loadBalancer.getName();
        try {
            Instance instance = namingService.selectOneHealthyInstance(serviceName, true);
            return new NacosServer(instance);
        } catch (NacosException e) {
            log.error("获取实例发送异常, {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }
}
