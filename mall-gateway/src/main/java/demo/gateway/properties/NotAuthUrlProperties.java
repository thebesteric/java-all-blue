package demo.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Data
@ConfigurationProperties("my.gateway")
public class NotAuthUrlProperties {
    private Set<String> shouldSkipUrls;
}
