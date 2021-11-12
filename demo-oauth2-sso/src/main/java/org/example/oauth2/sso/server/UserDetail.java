package org.example.oauth2.sso.server;

import lombok.Data;

@Data
public class UserDetail {
    private Long id;
    private String password;
    private String username;
}
