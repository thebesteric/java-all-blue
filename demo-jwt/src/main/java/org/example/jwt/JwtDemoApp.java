package org.example.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64Codec;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JwtDemoApp {

    public static final String SECRET_KEY = "123456";

    @Test
    public void createToken() {
        JwtBuilder builder = Jwts.builder()
                // 声明唯一身份标识 {"jti": "666"}
                .setId("666")
                // 声明主体 {"sub": "eric"}
                .setSubject("eric")
                // 声明创建日期 {"ita": "123456789"}
                .setIssuedAt(new Date())
                // 设置过期时间
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 1000))
                // payload
                .claim("roles", "admin")
                // payload
                .claim("logo", "xxx.jpg")
                // 签名方式
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY);

        final String token = builder.compact();
        System.out.println(token);

        System.out.println("=======");

        final String[] split = token.split("\\.");
        System.out.println(Base64Codec.BASE64.decodeToString(split[0]));
        System.out.println(Base64Codec.BASE64.decodeToString(split[1]));
        // 无法解密
        System.out.println(Base64Codec.BASE64.decodeToString(split[2]));


    }

    @Test
    public void parseToken() {
        String token = "eyJhbGciOiJIUzI1NiJ9" +
                ".eyJqdGkiOiI2NjYiLCJzdWIiOiJlcmljIiwiaWF0IjoxNjM1ODM5Njc1LCJleHAiOjE2MzU4Mzk3MzUsInJvbGVzIjoiYWRtaW4iLCJsb2dvIjoieHh4LmpwZyJ9" +
                ".1ATSe72RQOnCwHtYvCdpL8Y_58LpLyE8-h2MJ55MkU4";
        // 解析 token
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        System.out.println("id=" + claims.getId());
        System.out.println("subject=" + claims.getSubject());
        System.out.println("issuer=" + claims.getIssuer());
        System.out.println("issuedAt=" + claims.getIssuedAt());
        System.out.println("expiration=" + claims.getExpiration());
    }
}
