package org.example.redis.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class IndexController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/test")
    public void test() throws InterruptedException {
        int i = 0;
        while (true) {
            try {
                String key = "test-" + i;
                redisTemplate.opsForValue().set(key, i + "", 60, TimeUnit.SECONDS);
                i++;
                TimeUnit.SECONDS.sleep(1);
                if (i == 1000) return;
                System.out.println("添加成功: " + key);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

}
