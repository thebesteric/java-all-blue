package org.example.mall.user.controller;

import org.example.mall.comm.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${server.port}")
    private int port;

    @GetMapping("/get/{id}")
    public R getUser(@PathVariable String id) throws UnknownHostException {
        return R.success().setData("[" + InetAddress.getLocalHost().getHostAddress() + ":" + port + "] user-" + id);
    }
}
