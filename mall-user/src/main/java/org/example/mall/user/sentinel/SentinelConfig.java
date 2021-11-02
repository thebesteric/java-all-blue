package org.example.mall.user.sentinel;

import com.alibaba.csp.sentinel.adapter.servlet.CommonFilter;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SentinelConfig {

    @Bean
    public FilterRegistrationBean<CommonFilter> sentinelFilterRegistration() {
        FilterRegistrationBean<CommonFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new CommonFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        // 入口资源关闭聚合，解决链路不生效的问题
        filterRegistrationBean.addInitParameter(CommonFilter.WEB_CONTEXT_UNIFY, "false");
        filterRegistrationBean.setName("sentinelFilter");
        filterRegistrationBean.setOrder(1);

        // CommonFilter 的 BlockException 自定义处理逻辑
        WebCallbackManager.setUrlBlockHandler(new CommonUrlBlockHandler());

        return filterRegistrationBean;
    }
}
