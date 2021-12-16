package org.example.mall.order;

import com.alibaba.cloud.nacos.ribbon.NacosRule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.loadbalancer.IRule;
import feign.*;
import io.github.thebesteric.framework.switchlogger.annotation.EnableSwitchLogger;
import io.github.thebesteric.framework.switchlogger.core.domain.InvokeLog;
import io.github.thebesteric.framework.switchlogger.core.domain.RequestLog;
import io.github.thebesteric.framework.switchlogger.utils.JsonUtils;
import io.github.thebesteric.framework.switchlogger.utils.TransactionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.mall.anno.MyLoadBalanced;
import org.example.mall.order.intercept.FeignAuthInterceptor;
import org.example.mall.order.intercept.MyLoadBalancerInterceptor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SpringBootApplication
@EnableDiscoveryClient // 开启 nacos client（可以不用加）
@EnableFeignClients // 开启 feign client
// 不推荐使用 @RibbonClients 因为 configuration 所指定的配置类，必须不能被 spring 所扫描到
// @RibbonClients(value = {
//         @RibbonClient(name = "mall-user", configuration = RibbonRuleConfig.class)
// })
@EnableSwitchLogger
public class OrderApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(OrderApp.class, args);
        System.out.println(context);
    }

    @Bean
    @LoadBalanced
    // @MyLoadBalanced
    public RestTemplate restTemplate(LoadBalancerClient loadBalancerClient) {
        return new RestTemplate();

        // 如果需要使用 service-name 进行调用的话，需要增加 LoadBalancerInterceptor
        // 这个也是 @LoadBalanced 的实现方式
        // RestTemplate restTemplate = new RestTemplate();
        // restTemplate.setInterceptors(Collections.singletonList(new LoadBalancerInterceptor(loadBalancerClient)));
        // return restTemplate;
    }

    // 这种方式是全局配置，会对所有微服务生效
    // 如果要使用局部配置，请在 yml 文件中配置
    // @Bean
    public IRule ribbonRule() {
        // 指定使用 nacos 提供的负载均衡策略（优先调用同一集群的实例，基于随机权重）
        return new NacosRule();
        // return new VersionRule();
    }

    // @Configuration
    public static class MyLoadBalancerConfiguration {

        @Autowired(required = false)
        @MyLoadBalanced // 限定注入属性
        public List<RestTemplate> restTemplates = Collections.emptyList();

        @Bean
        public MyLoadBalancerInterceptor myLoadBalancerInterceptor(LoadBalancerClient loadBalancerClient) {
            return new MyLoadBalancerInterceptor(loadBalancerClient);
        }

        @Bean
        public SmartInitializingSingleton myLoadBalancedRestTemplateInitializer(MyLoadBalancerInterceptor myLoadBalancerInterceptor) {
            return () -> {
                for (RestTemplate restTemplate : restTemplates) {
                    restTemplate.setInterceptors(Collections.singletonList(myLoadBalancerInterceptor));
                }
            };
        }
    }

    // 全局配置，如果单独给某个微服务配置，需要在 @FeignClient 配置 configuration 属性，并注释 @Configuration
    // @FeignClient(value = "mall-user", path = "/user", configuration = OrderApp.FeignConfiguration.class)
    @Configuration // 不推荐，容易配置成全局
    public static class FeignConfiguration {

        @Bean
        public Request.Options options() {
            return new Request.Options(5000, 10000);
        }

        @Bean
        public Logger logger() {
            return new FeignLog(this.getClass());
        }

        @Bean
        public Logger.Level feignLogLevel() {
            // NONE: 不输出日志
            // BASIC: 只输出请求方法的 URL，响应状态码，接口执行时间
            // HEADERS: BASIC 级别的信息 + 请求头信息
            // FULL: 完整的信息
            return Logger.Level.FULL;
        }

        // Feign 拦截器
        @Bean
        public RequestInterceptor basicAuthRequestInterceptor() {
            // 开始 Basic 认证，会自动在请求头添加 Authorization: Basic ZXJpYzoxMjM=
            // return new BasicAuthRequestInterceptor("eric", "123");

            // 自定义拦截器
            return new FeignAuthInterceptor();
        }

        static final class FeignLog extends feign.Logger {

            private final Log log;
            private final Boolean recordLog;

            private final static ThreadLocal<RequestLog> requestLogThreadLocal = new ThreadLocal<>();

            public FeignLog(Class<?> clazz) {
                log = LogFactory.getLog(clazz);
                this.recordLog = log.isInfoEnabled();
            }

            @Override
            protected void log(String configKey, String format, Object... args) {
                String trackId = TransactionUtils.get();
                log.info(String.format(methodTag(configKey) + format, args));
            }

            @Override
            protected void logRequest(String configKey, Level logLevel, Request request) {
                RequestLog requestLog = new RequestLog();
                requestLog.setCreatedTime(System.currentTimeMillis());
                requestLog.setTag("feign");
                requestLog.setLevel(RequestLog.LEVEL_INFO);
                requestLog.setTrackId(TransactionUtils.get());
                requestLog.setRequestSessionId(TransactionUtils.get());

                if (request.requestTemplate() != null) {

                    // uri & url
                    String url = request.requestTemplate().url();
                    requestLog.setUrl(url);
                    String[] arr = url.split("//");
                    if (arr.length > 1) {
                        String uri = arr[1].substring(arr[1].indexOf("/"));
                        requestLog.setUri(uri);
                    }

                    // uri info
                    URI uri = URI.create(url);
                    requestLog.setProtocol(uri.getScheme());
                    requestLog.setServerName(uri.getHost());
                    requestLog.setRemoteAddr(uri.getHost());
                    requestLog.setRemotePort(uri.getPort());
                    requestLog.setQueryString(uri.getQuery());

                    // request body
                    requestLog.setRawBody(request.body() == null ? null : new String(request.body(), StandardCharsets.UTF_8));
                    if (requestLog.getRawBody() != null) {
                        try {
                            requestLog.setBody(JsonUtils.mapper.readValue(requestLog.getRawBody(), Map.class));
                        } catch (JsonProcessingException e) {
                            log.warn(String.format("cannot parse body to json: %s", requestLog.getRawBody()));
                        }
                    }

                    // url params
                    final HashMap<String, String> params = new LinkedHashMap<>();
                    if (requestLog.getUrl() != null && requestLog.getUrl().split("\\?").length > 1) {
                        final String[] urlSplitArr = requestLog.getUrl().split("\\?");
                        final String[] paramKeyValueArr = urlSplitArr[1].split("&");
                        for (String keyValue : paramKeyValueArr) {
                            final String[] keyValueArr = keyValue.split("=");
                            params.put(keyValueArr[0], keyValueArr.length > 1 ? keyValueArr[1] : null);
                        }
                    }
                    requestLog.setParams(params);

                    // headers
                    Map<String, String> headers = new HashMap<>();
                    request.requestTemplate().headers().forEach((key, values) -> {
                        String value = String.join(",", values);
                        headers.put(key, value);
                    });
                    requestLog.setHeaders(headers);

                    // http method
                    requestLog.setMethod(request.httpMethod().name());

                    // execute info
                    InvokeLog.ExecuteInfo executeInfo = new InvokeLog.ExecuteInfo();
                    executeInfo.setStartTime(System.currentTimeMillis());

                    // class info
                    if (request.requestTemplate().feignTarget() != null && request.requestTemplate().feignTarget().type() != null) {
                        Class<?> clazz = request.requestTemplate().feignTarget().type();
                        executeInfo.setClassName(clazz.getName());
                    }

                    // method info
                    if (request.requestTemplate().methodMetadata() != null && request.requestTemplate().methodMetadata().returnType() != null) {
                        InvokeLog.ExecuteInfo.MethodInfo methodInfo = new InvokeLog.ExecuteInfo.MethodInfo();
                        Method method = request.requestTemplate().methodMetadata().method();
                        methodInfo.setMethodName(method.getName());
                        methodInfo.setReturnType(method.getReturnType().getName());

                        // method signatures
                        final LinkedHashMap<String, Object> methodSignatures = new LinkedHashMap<>();
                        Parameter[] parameters = method.getParameters();
                        for (Parameter parameter : parameters) {
                            methodSignatures.put(parameter.getName(), parameter.getParameterizedType().getTypeName());
                        }
                        methodInfo.setSignatures(methodSignatures);

                        // method arguments
                        final LinkedHashMap<String, Object> methodArgs = new LinkedHashMap<>();
                        if (request.requestTemplate().methodMetadata().indexToName() != null && request.requestTemplate().methodMetadata().indexToName().size() > 0) {
                            for (Map.Entry<Integer, Collection<String>> entry : request.requestTemplate().methodMetadata().indexToName().entrySet()) {
                                final String paramName = entry.getValue().toArray()[0].toString();
                                final Collection<String> paramValues = request.headers().getOrDefault(paramName, null);
                                String paramValue = null;
                                if (paramValues != null && paramValues.size() > 0) {
                                    paramValue = paramValues.toArray()[0].toString();
                                }
                                if (paramValue == null && params.containsKey(paramName)) {
                                    paramValue = params.get(paramName);
                                }
                                methodArgs.put(paramName, paramValue);
                            }
                            methodInfo.setArguments(methodArgs);
                        }

                        // set MethodInfo
                        executeInfo.setMethodInfo(methodInfo);
                    }

                    requestLog.setExecuteInfo(executeInfo);
                }

                System.out.println(requestLog);
                requestLogThreadLocal.set(requestLog);
            }

            @Override
            protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
                final RequestLog requestLog = requestLogThreadLocal.get();
                requestLogThreadLocal.remove();
                requestLog.setDuration(elapsedTime);
                int status = response.status();
                if (response.body() != null) {
                    byte[] bodyData = feign.Util.toByteArray(response.body().asInputStream());
                    if (bodyData.length > 0) {
                        String responseBody = feign.Util.decodeOrDefault(bodyData, feign.Util.UTF_8, "Binary data");
                        requestLog.setResult(responseBody);
                    }
                    if (status != 200 && status != 100) {
                        requestLog.setLevel(RequestLog.LEVEL_ERROR);
                        log.error(requestLog);
                    } else {
                        log.info(requestLog);
                    }
                    return response.toBuilder().body(bodyData).build();
                }
                log.error(requestLog);
                return response;
            }

            @Override
            protected IOException logIOException(String configKey, Level logLevel, IOException ioe, long elapsedTime) {
                final RequestLog requestLog = requestLogThreadLocal.get();
                requestLogThreadLocal.remove();
                requestLog.setLevel(RequestLog.LEVEL_ERROR);
                requestLog.setException(ioe.getMessage());
                requestLog.setDuration(elapsedTime);
                log.error(requestLog);
                return ioe;
            }
        }
    }


}
