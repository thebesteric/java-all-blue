package com.example.demo.elasticsearch.service;

import com.example.demo.elasticsearch.pojo.SysLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class SysLogService {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public List<SysLog> selectPage(Integer page, Integer size) throws JsonProcessingException {
        // 查询最近 15 分钟内的日志
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -15);

        Query query = new NativeSearchQuery(QueryBuilders.rangeQuery("@timestamp").gte(calendar.getTime()));
        query.setPageable(PageRequest.of(page - 1, size));
        SearchHits<SysLog> search = elasticsearchRestTemplate.search(query, SysLog.class);

        List<SysLog> logs = new ArrayList<>();
        for (SearchHit<SysLog> searchHit : search.getSearchHits()) {
            SysLog sysLog = searchHit.getContent();

            ObjectMapper objectMapper = new ObjectMapper();
            SysLog.MessagePojo messagePojo = objectMapper.readValue(sysLog.getMessage(), SysLog.MessagePojo.class);
            sysLog.setMessagePojo(messagePojo);

            logs.add(sysLog);
        }
        return logs;
    }
}
