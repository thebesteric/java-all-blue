package demo.gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CheckAuthGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {
    @Override
    public GatewayFilter apply(NameValueConfig config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                log.info("调用 CheckAuthGatewayFilterFactory: " + config.getName() + "=" + config.getValue());
                // 比如可以从这里拿到值，往 header 里添加
                ServerHttpRequest request = exchange.getRequest().mutate().header(config.getName(), config.getValue()).build();
                exchange = exchange.mutate().request(request).build();
                return chain.filter(exchange);
            }
        };
    }
}
