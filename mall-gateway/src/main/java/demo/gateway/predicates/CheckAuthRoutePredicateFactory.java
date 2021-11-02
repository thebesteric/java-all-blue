package demo.gateway.predicates;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * 自定义路由断言工厂的命名规范必须符合 XxxRoutePredicateFactory
 * Xxx 就是该工厂的 name 属性
 * CheckAuthRoutePredicateFactory 的 name = CheckAuth
 *
 * 标准配置：
 * - name: CheckAuth
 *   agrs:
 *     name: eric
 * 快捷配置：
 * - CheckAuth: eric
 */
@Component
@Slf4j
public class CheckAuthRoutePredicateFactory extends AbstractRoutePredicateFactory<CheckAuthRoutePredicateFactory.Config> {


    public CheckAuthRoutePredicateFactory() {
        super(Config.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                log.info("调用 CheckAuthRoutePredicateFactory: " + config.getName());
                return config.getName().equals("eric");
            }
        };
    }

    /**
     * 快捷配置
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("name");
    }

    /**
     * 需要封装一个内部类，该类用于封装 application.yml 中的配置
     */
    @Getter
    @Setter
    public static class Config {
        private String name;
    }
}
