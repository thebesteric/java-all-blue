package org.example.demo.demo;

import java.util.UUID;

/**
 * UserManageInterceptor1
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-08-30 23:31:06
 */
public class UserManageInterceptor2 {

    public String getUsername(Long id) {
        return "UserManageInterceptor2 用户 ID：" + id + " 的名字是：" + UUID.randomUUID();
    }
}
