package org.example.oauth2.sso.server.config;

import org.example.oauth2.sso.server.UserDetail;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

// 用来封装从数据库查询出来的真实用户
public class MemberDetails implements UserDetails {

    private UserDetail userDetail;

    // user 是从数据库或RPC查询出来
    public MemberDetails(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 查询数据库，返回用户权限
        return Arrays.asList(new SimpleGrantedAuthority("TEST"));
    }

    @Override
    public String getPassword() {
        return userDetail.getPassword();
    }

    @Override
    public String getUsername() {
        return userDetail.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserDetail getUser() {
        return userDetail;
    }
}
