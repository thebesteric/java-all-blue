package com.example.demo.elasticsearch;

import com.example.demo.elasticsearch.pojo.SysLog;
import com.example.demo.elasticsearch.service.SysLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SysLogTest {

    @Autowired
    private SysLogService sysLogService;

    @Test
    void getLogs() throws JsonProcessingException {
        List<SysLog> sysLogs = sysLogService.selectPage(1, 10);
        for (SysLog sysLog : sysLogs) {
            System.out.println(sysLog);
        }
    }
}
