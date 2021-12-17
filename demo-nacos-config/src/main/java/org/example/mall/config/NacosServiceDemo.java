package org.example.mall.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.listener.Listener;

import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class NacosServiceDemo {

    public static void main(String[] args) throws Exception {

        // System.out.println(new BCryptPasswordEncoder().encode("nacos@tjb.com"));

        String serverAddr = "127.0.0.1:8848";
        String dataId = "config-demo.yml";
        String group = "DEFAULT_GROUP";
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);

        // 获取配置中心服务
        ConfigService configService = NacosFactory.createConfigService(properties);

        // 从配置中心拉取配置
        String content = configService.getConfig(dataId, group, 5000);
        System.out.println(content);

        // 注册监听器
        configService.addListener(dataId, group, new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String info) {
                System.out.println("感知到变化:" + info);
            }
        });

        // 发布配置
        boolean succeed = configService.publishConfig(dataId, group, "common: age: 30", ConfigType.YAML.getType());
        if (succeed) {
            System.out.println("发布成功");
            TimeUnit.SECONDS.sleep(5);

            // 从配置中心拉取配置
            content = configService.getConfig(dataId, group, 5000);
            System.out.println(content);
        } else {
            System.out.println("发布失败");
        }

    }
}
