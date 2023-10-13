package org.example.springboot.controller;

import io.github.thebesteric.framework.apm.agent.extension.annotation.ApmTracing;
import org.example.springboot.feign.BaiduFeign;
import org.example.springboot.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * TestController
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-18 18:15:19
 */
@RestController
@RequestMapping("/test")
@ApmTracing
public class TestController {

    @Resource
    private TestService testService;

    @Resource
    private BaiduFeign baiduFeign;

    @ApmTracing
    @GetMapping("/list")
    public Object list() {
        return testService.selectList();
    }

    @ApmTracing
    @GetMapping("/page")
    public Object page(int pageNum, int pageSize) {
        String baidu = baiduFeign.baidu();
        return testService.selectPage(pageNum, pageSize);
    }

}
