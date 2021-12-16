package org.example.feign.demo;

import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FeignDemo {
    public static void main(String[] args) {
        FeignServiceDemo feignServiceDemo = Feign.builder()
                // 编码方式
                .encoder(new JacksonEncoder())
                // 解码方式
                .decoder(new JacksonDecoder())
                .options(new Request.Options(1000, TimeUnit.MILLISECONDS, 3500, TimeUnit.MILLISECONDS, true))
                // 重试机制
                .retryer(new Retryer.Default(5000, 5000, 3))
                // 目标
                .target(FeignServiceDemo.class, "http://127.0.0.1:8020");
        // GET
        System.out.println(feignServiceDemo.getAccount("1"));

        // POSt
        Map<String, Object> params = new HashMap<>();
        params.put("blance", 100.00D);
        params.put("total", 200.00D);
        System.out.println(feignServiceDemo.saveAccount(params));
    }
}
