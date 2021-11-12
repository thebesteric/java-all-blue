package org.example.oauth2.sso.server.config;

import lombok.extern.slf4j.Slf4j;
import org.example.oauth2.sso.server.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // final String password = passwordEncoder.encode("123456");
        // return new User("test", password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        return getByUsername(username);
    }

    public MemberDetails getByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            log.warn("登录名为空");
            throw new UsernameNotFoundException("登录名为空");
        }
        // TODO 查数据库 或者 RPC 远程调用
        UserDetail userDetail = new UserDetail();
        userDetail.setId(1000L);
        userDetail.setUsername("test");
        userDetail.setPassword(passwordEncoder.encode("123456"));
        return new MemberDetails(userDetail);
    }
}
