package org.example.demo.demo;

import java.util.UUID;

/**
 * UserManager
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-08-29 00:19:42
 */
public class UserManager {

    public UserManager() {
        System.out.println("UserManager 无参构造方法执行");
    }

    public UserManager(String h) {
        System.out.println("UserManager 含参构造方法执行");
    }

    public String getUsername(Long id) {
        return "用户 ID：" + id + " 的名字是：" + UUID.randomUUID();
    }

    public int getAge() {
        return 41;
    }

    public void print() {
        System.out.println("hello world");
    }



}
