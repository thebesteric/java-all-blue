package org.example.oauth2.sso.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class JwtConfig {

    @Bean
    public TokenStore jwtTokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        // 配置 jwt 使用的密钥，这个密钥需要给到下面的服务，来对 token 进行校验
        jwtAccessTokenConverter.setSigningKey("123456");
        return jwtAccessTokenConverter;
    }

    // @Bean
    // public KeyPair keyPair() {
    //     KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(jwtCAProperties.getKeyPairName()), jwtCAProperties.getKeyPairSecret().toCharArray());
    //     return keyStoreKeyFactory.getKeyPair(jwtCAProperties.getKeyPairAlias(), jwtCAProperties.getKeyPairStoreSecret().toCharArray());
    // }

    // 对 jwt 对一个增强，可以加入其他信息
    @Bean
    public JwtTokenEnhancer jwtTokenEnhancer() {
        return new JwtTokenEnhancer();
    }

    public static class JwtTokenEnhancer implements TokenEnhancer {

        @Override
        public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {

            MemberDetails memberDetail = (MemberDetails) oAuth2Authentication.getPrincipal();

            Map<String, Object> info = new HashMap<>();
            // 这里可以设置相关信息
            info.put("userId", memberDetail.getUser().getId());
            info.put("other", "other info");
            ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(info);
            return oAuth2AccessToken;
        }
    }

}
