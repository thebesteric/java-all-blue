package org.example.mall.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class NacosPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String pwd = encoder.encode("tjb@nacos");
        System.out.println(pwd);
    }
}
