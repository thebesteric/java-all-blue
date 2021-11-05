package org.example.oauth.controller;

import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@RestController
public class JwtTokenController {

    @GetMapping("/parseJwtToken")
    public Object parseJwtToken(Authentication authentication, HttpServletRequest request) {
        final String authorization = request.getHeader("Authorization");
        String token;
        if (authorization != null) {
            token = authorization.substring(authorization.indexOf("bearer") + 7);
        } else {
            token = request.getParameter("access_token");
        }
        return Jwts.parser().setSigningKey("123456".getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }

}
