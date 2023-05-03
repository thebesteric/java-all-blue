package com.example.demo.elasticsearch;

import com.example.demo.elasticsearch.pojo.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;

@SpringBootTest
class DemoElasticsearchApplicationTests {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 创建索引
     */
    @Test
    void createIndex() {
        // Spring Date 4.x 之后都是用 IndexOperations 操作 ES
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(Item.class);

        // 创建索引 同时 创建 mapping
        boolean result = indexOperations.createWithMapping();
        System.out.println("create index: " + result);
    }

    /**
     * 删除索引
     */
    @Test
    void deleteIndex() {
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(Item.class);
        if (indexOperations.exists()) {
            boolean result = indexOperations.delete();
            System.out.println("delete index: " + result);
        }
    }
}
