package org.example.springboot;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * SpringConfig
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-10-10 15:53:28
 */
@Configuration
public class SpringConfig {

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        // 连接超时
        requestFactory.setConnectTimeout(5000);
        //读超时
        requestFactory.setReadTimeout(10000);
        //连接池获取连接超时时间
        requestFactory.setConnectionRequestTimeout(5000);

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //Httpclient连接池的方式，同时支持netty，okHttp以及其他http框架
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        // 最大连接数
        connectionManager.setMaxTotal(200);
        // 同路由并发数
        connectionManager.setDefaultMaxPerRoute(50);
        //配置连接池
        httpClientBuilder.setConnectionManager(connectionManager);

        // 最大连接数
        connectionManager.setMaxTotal(500);
        // 同路由并发数（每个主机的并发）
        connectionManager.setDefaultMaxPerRoute(100);
        httpClientBuilder.setConnectionManager(connectionManager);
        requestFactory.setHttpClient(httpClientBuilder.build());
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

}
