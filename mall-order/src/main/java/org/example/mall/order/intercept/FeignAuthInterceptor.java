package org.example.mall.order.intercept;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.UUID;

public class FeignAuthInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String accessToken = UUID.randomUUID().toString();
        requestTemplate.header("Authorization", accessToken);
    }
}
