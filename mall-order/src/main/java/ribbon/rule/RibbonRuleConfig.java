package ribbon.rule;

import com.alibaba.cloud.nacos.ribbon.NacosRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonRuleConfig {
    @Bean
    public IRule ribbonRule() {
        // 指定使用 nacos 提供的负载均衡策略（优先调用同一集群的实例，基于随机权重）
        return new NacosRule();
        // return new VersionRule();
    }

}
