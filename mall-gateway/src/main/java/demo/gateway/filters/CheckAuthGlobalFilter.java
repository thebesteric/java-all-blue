package demo.gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * GlobalFilter 不需要配置
 */
@Component
@Order(-1)
@Slf4j
public class CheckAuthGlobalFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> token = exchange.getRequest().getHeaders().get("token");
        log.info("token = " + token);
        if (token == null || token.isEmpty()) {
            return chain.filter(exchange);
        }
        // TODO token 校验
        return chain.filter(exchange);
    }
}
