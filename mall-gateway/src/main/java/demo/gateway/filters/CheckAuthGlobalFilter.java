package demo.gateway.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.gateway.properties.NotAuthUrlProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * GlobalFilter 不需要配置
 */
@Component
@Order(-1)
@Slf4j
@EnableConfigurationProperties(NotAuthUrlProperties.class)
public class CheckAuthGlobalFilter implements GlobalFilter {

    @Autowired
    private NotAuthUrlProperties notAuthUrlProperties;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        final String currentPath = exchange.getRequest().getURI().getPath();
        // 1、过滤不需要的 url
        if (shouldSkip(currentPath)) {
            log.info("跳过不需要验证的 url：{}", currentPath);
            return chain.filter(exchange);
        }


        // 2、获取 token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StringUtils.isEmpty(token)) {
            token = exchange.getRequest().getQueryParams().getFirst("access_token");
        } else {
            token = token.substring(7);
        }
        if (StringUtils.isEmpty(token)) {
            log.warn("需要验证的 url：{}, 没有包含 token", currentPath);
            throw new RuntimeException("需要验证的 url, 没有包含 token");
        }
        log.info("token = " + token);

        // 3、校验 token
        Claims claims = Jwts.parser()
                .setSigningKey(generalKey())
                .parseClaimsJws(token)
                .getBody();

        // 4、加入请求头
        ServerWebExchange webExchange = warpHeader(exchange, claims);

        return chain.filter(webExchange);
    }

    public static SecretKey generalKey() {
        String encodedKey = "123456";
        byte [] encodeKeyChar = encodedKey.getBytes();
        return new SecretKeySpec(encodeKeyChar, 0, encodeKeyChar.length, "AES");
    }

    ObjectMapper mapper = new ObjectMapper();

    private ServerWebExchange warpHeader(ServerWebExchange exchange, Claims claims) throws JsonProcessingException {
        final String str = mapper.writeValueAsString(claims);
        log.info("jwt 中的用户信息：{}", str);

        String userId = claims.get("userId").toString();

        ServerHttpRequest request = exchange.getRequest().mutate()
                .header("x-user-id", userId)
                .build();
        // 将 request 构建为 ServerWebExchange 对象
        return exchange.mutate().request(request).build();
    }

    private boolean shouldSkip(String currentPath) {
        PathMatcher pathMatcher = new AntPathMatcher();
        for (String skipUrl : notAuthUrlProperties.getShouldSkipUrls()) {
            if (pathMatcher.match(skipUrl, currentPath)) {
                return true;
            }
        }
        return false;
    }
}
