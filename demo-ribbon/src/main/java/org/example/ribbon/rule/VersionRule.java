package org.example.ribbon.rule;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.ExtendBalancer;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class VersionRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    @SuppressWarnings("rawtypes")
    public Server choose(Object key) {
        try {
            Map<String, String> metadata = this.nacosDiscoveryProperties.getMetadata();
            DynamicServerListLoadBalancer loadBalancer = (DynamicServerListLoadBalancer) this.getLoadBalancer();
            String name = loadBalancer.getName();
            NamingService namingService = this.nacosDiscoveryProperties.namingServiceInstance();
            List<Instance> instances = namingService.selectInstances(name, true);

            if (CollectionUtils.isEmpty(instances)) {
                log.warn("no instance in service {}", name);
            } else {
                List<Instance> instancesToChoose = instances;
                if (metadata != null) {
                    String version = metadata.get("version");
                    if (StringUtils.isNotBlank(version)) {
                        // 过滤版本相同的实例
                        List<Instance> sameVersionInstances = instances.stream().filter((instancex) ->
                                Objects.equals(version, instancex.getMetadata().get("version"))).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(sameVersionInstances)) {
                            instancesToChoose = sameVersionInstances;
                        } else {
                            log.warn("A cross-cluster call occurs，name = {}, version = {}, instance = {}", name, version, instances);
                        }
                    }
                }
                Instance instance = ExtendBalancer.getHostByRandomWeight2(instancesToChoose);
                return new NacosServer(instance);
            }

        } catch (NacosException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }
}
