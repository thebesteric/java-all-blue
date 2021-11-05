package org.example.oauth2.sso.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManagerBean;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenStore jwtTokenStore;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private JwtConfig.JwtTokenEnhancer jwtTokenEnhancer;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                // 配置 client_id
                .withClient("client")
                // 配置 client_secret
                .secret(passwordEncoder.encode("123456"))
                // 配置 token 有效期
                .accessTokenValiditySeconds(3600)
                // 配置 refresh token 有效期
                .refreshTokenValiditySeconds(86400)
                // 配置回调地址
                .redirectUris("http://127.0.0.1:8081/login", "http://127.0.0.1:8082/login")
                // 自动同意
                .autoApprove(true)
                // 配置权限范围
                .scopes("all")
                // 配置 grant_type 授权类型
                // authorization_code：授权码模式
                // implicit：简化模式
                // password：密码模式
                // client_credentials：客户端模式
                // refresh_token：更新令牌
                .authorizedGrantTypes("authorization_code", "password", "refresh_token");
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // 允许表单验证
                .allowFormAuthenticationForClients()
                // 获取密钥需要身份认证，使用单点登录时必须配置
                .tokenKeyAccess("isAuthenticated()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        // 配置 JWT 的内容增强器
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(jwtTokenEnhancer);
        delegates.add(jwtAccessTokenConverter);
        enhancerChain.setTokenEnhancers(delegates);

        endpoints.authenticationManager(authenticationManagerBean)
                .tokenStore(jwtTokenStore)
                // token 转换器
                .accessTokenConverter(jwtAccessTokenConverter)
                // 配置 token 增强
                .tokenEnhancer(enhancerChain)
                // 配置更新令牌是否可以重复使用
                .reuseRefreshTokens(false)
                // 刷新令牌授权包含对用户信息对检查
                .userDetailsService(userService)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }
}
