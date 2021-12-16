package org.example.feign;

import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.feign.interceptor.FeignAuthInterceptor;
import org.example.feign.service.AccountFeignService;
import org.example.mall.comm.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableFeignClients
public class AppFeign {
    public static void main(String[] args) {
        SpringApplication.run(AppFeign.class, args);
    }

    @RestController
    public static class FeignController {

        @Autowired
        private AccountFeignService accountFeignService;

        @GetMapping("get/{id}")
        public R getAccount(@PathVariable String id) {
            return accountFeignService.get(id);
        }

        @PostMapping("save")
        public R saveAccount(@RequestBody Map<String, Object> params) {
            return accountFeignService.save(params);
        }
    }

    @Configuration
    public static class FeignConfiguration {

        // 配置超时时间
        @Bean
        public Request.Options options() {
            return new Request.Options(5000, TimeUnit.MILLISECONDS,
                    10000, TimeUnit.MILLISECONDS, true);
        }

        // 配置日志打印，此时需要开启 debug 模式
        @Bean
        public Logger.Level feignLogLevel() {
            // NONE: 不输出日志
            // BASIC: 只输出请求方法的 URL，响应状态码，接口执行时间
            // HEADERS: BASIC 级别的信息 + 请求头信息
            // FULL: 完整的信息
            return Logger.Level.FULL;
        }

        // 使用原生的 feign 注解
        // @Bean
        public Contract feignContract() {
            // feign 支持 SpringMVC 注解的 Contract
            // return new SpringMvcContract();
            return new Contract.Default();
        }

        // Feign 拦截器
        @Bean
        public RequestInterceptor basicAuthRequestInterceptor() {
            // 开始 Basic 认证，会自动在请求头添加 Authorization: Basic ZXJpYzoxMjM=
            // return new BasicAuthRequestInterceptor("eric", "123");

            // 自定义拦截器
            return new FeignAuthInterceptor();
        }

        // 解码器
        @Bean
        public Decoder decoder() {
            return new JacksonDecoder();
        }

        // 编码器
        @Bean
        public Encoder encoder() {
            return new JacksonEncoder();
        }

        // 自定义 feign 日志
        // @Bean
        public Logger logger() {
            return new FeignLog(this.getClass());
        }

        static final class FeignLog extends feign.Logger {

            private final Log log;

            public FeignLog(Class<?> clazz) {
                log = LogFactory.getLog(clazz);
            }

            @Override
            protected void log(String configKey, String format, Object... args) {
                log.info(String.format(methodTag(configKey) + format, args));
            }

            @Override
            protected void logRequest(String configKey, Level logLevel, Request request) {

            }

            @Override
            protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
                return response;
            }

            @Override
            protected IOException logIOException(String configKey, Level logLevel, IOException ioe, long elapsedTime) {
                return ioe;
            }
        }
    }


}
