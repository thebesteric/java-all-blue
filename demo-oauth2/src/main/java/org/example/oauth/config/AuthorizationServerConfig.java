package org.example.oauth.config;

import org.example.oauth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 授权服务器
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        /**
         * 【授权码模式】
         * http://127.0.0.1:8080/oauth/authorize?response_type=code&client_id=client&redirect_uri=http://www.baidu.com&scope=all
         * ==> 获取 code
         * https://www.baidu.com/?code=s5tuZz
         * ==> 获取 access_token
         * http://127.0.0.1:8080/oauth/token?grant_type=authorization_code&client_id=client&client_secret=123456&scope=all&redirect_uri=http://www.baidu.com&code=s5tuZz
         * ==> 访问资源
         * http://127.0.0.1:8080/user/getCurrentUser?access_token=a61dc889-b893-4c1e-a605-be706f1c83af
         *
         * 【简化模式】
         * http://127.0.0.1:8080/oauth/authorize?response_type=token&client_id=client&redirect_uri=http://www.baidu.com&scope=all
         * ==> 直接返回 access_token
         * https://www.baidu.com/#access_token=33688977-0203-4b2f-aaad-b611fa34c4e1&token_type=bearer&expires_in=3599
         * ==> 访问资源
         * http://127.0.0.1:8080/user/getCurrentUser?access_token=a61dc889-b893-4c1e-a605-be706f1c83af
         *
         * 【密码模式】
         * http://127.0.0.1:8080/oauth/token?username=test&password=123456&grant_type=password&client_id=client&client_secret=123456&scope=all
         * ==> 访问资源
         * http://127.0.0.1:8080/user/getCurrentUser?access_token=a61dc889-b893-4c1e-a605-be706f1c83af
         *
         * 【客户端模式】
         * http://127.0.0.1:8080/oauth/token?grant_type=client_credentials&client_id=client&client_secret=123456&scope=all
         * ==> 访问资源
         * http://127.0.0.1:8080/user/getCurrentUser?access_token=a61dc889-b893-4c1e-a605-be706f1c83af
         */

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
                .redirectUris("http://www.baidu.com")
                // 配置权限范围
                .scopes("all")
                // 自动同意
                .autoApprove(true)
                // 配置 grant_type 授权类型
                // authorization_code：授权码模式
                // implicit：简化模式
                // password：密码模式
                // client_credentials：客户端模式
                // refresh_token：更新令牌
                .authorizedGrantTypes("authorization_code", "implicit", "password", "client_credentials", "refresh_token");
                // .authorizedGrantTypes("authorization_code", "implicit");
    }

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManagerBean;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManagerBean)
                .userDetailsService(userService)
                .tokenStore(tokenStore)
                // 配置更新令牌是否可以重复使用
                .reuseRefreshTokens(false)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }
}
