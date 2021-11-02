package org.example.mall.order.feign;

import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public class FeignDemo {
    public static void main(String[] args) {
        FeignServiceDemo feignServiceDemo = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(1000, 3500))
                .retryer(new Retryer.Default(5000,5000,3))
                .target(FeignServiceDemo.class, "http://127.0.0.1:8010");
        System.out.println(feignServiceDemo.getUser("1"));
    }
}
