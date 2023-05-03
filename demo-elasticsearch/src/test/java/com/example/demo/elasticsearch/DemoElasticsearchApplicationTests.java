package com.example.demo.elasticsearch;

import com.example.demo.elasticsearch.pojo.Item;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.apache.lucene.search.BooleanQuery;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

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

        // 判断索引是否存在
        if (!indexOperations.exists()) {
            // 创建索引 同时 创建 mapping
            boolean result = indexOperations.createWithMapping();
            System.out.println("create index: " + result);
        }
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

    /**
     * 新增文档
     */
    @Test
    void addDocument() {
        // id 不设置则 ES 会自动计算，设置则使用指定指
        Item item = new Item("高端智能手机", 780000L, "手机");
        item = elasticsearchRestTemplate.save(item);
        System.out.println(item);
    }

    /**
     * 批量新增文档
     */
    @Test
    void addDocuments() {
        List<Item> items = List.of(
                new Item("苹果14pro智能手机", 980000L, "手机"),
                new Item("华为Meta50智能手机", 568000L, "手机"));
        Iterable<Item> iterable = elasticsearchRestTemplate.save(items);
        Iterator<Item> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    /**
     * 修改文档（全量替换）
     */
    @Test
    void updateDocument() {
        Item item = new Item("o9744IcBf2Sl2md7JmWm", "华为Meta50智能手机", 568000L, "手机");
        item = elasticsearchRestTemplate.save(item);
        System.out.println(item);
    }

    /**
     * 删除文档
     */
    @Test
    void deleteDocument() {
        // 使用 id 删除
        String id = elasticsearchRestTemplate.delete("p97_4IcBf2Sl2md7KmVG", Item.class);
        System.out.println("delete document: " + id);

        // 使用对象删除
        id = elasticsearchRestTemplate.delete(new Item("pN794IcBf2Sl2md7dmWt"));
        System.out.println("delete document: " + id);
    }

    /**
     * 根据主键查询
     */
    @Test
    void searchById() {
        Item item = elasticsearchRestTemplate.get("pN794IcBf2Sl2md7dmWt", Item.class);
        System.out.println(item);
    }

    /**
     * 全字段匹配
     */
    @Test
    void search() {
        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery("高端智能手机");
        Query query = new NativeSearchQuery(queryBuilder);

        SearchHits<Item> searchHits = elasticsearchRestTemplate.search(query, Item.class);

        List<SearchHit<Item>> list = searchHits.getSearchHits();

        for (SearchHit<Item> searchHit : list) {
            System.out.println(searchHit.getContent());
        }
    }

    /**
     * match all
     * 相当于 list
     */
    @Test
    void searchMatchAllQuery() {
        MatchAllQueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        Query query = new NativeSearchQuery(queryBuilder);

        SearchHits<Item> searchHits = elasticsearchRestTemplate.search(query, Item.class);

        List<SearchHit<Item>> list = searchHits.getSearchHits();

        for (SearchHit<Item> searchHit : list) {
            System.out.println(searchHit.getContent());
        }
    }

    /**
     * match query
     * 匹配一个字段
     */
    @Test
    void searchMatchQuery() {
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("title", "高端智能");
        Query query = new NativeSearchQuery(queryBuilder);

        SearchHits<Item> searchHits = elasticsearchRestTemplate.search(query, Item.class);

        List<SearchHit<Item>> list = searchHits.getSearchHits();

        for (SearchHit<Item> searchHit : list) {
            System.out.println(searchHit.getContent());
        }
    }

    /**
     * range query
     * 范围查询
     */
    @Test
    void searchMatchPhraseQuery() {
        RangeQueryBuilder queryBuilder = QueryBuilders.rangeQuery("price").gte(500000).lte(900000);
        Query query = new NativeSearchQuery(queryBuilder);

        SearchHits<Item> searchHits = elasticsearchRestTemplate.search(query, Item.class);

        List<SearchHit<Item>> list = searchHits.getSearchHits();

        for (SearchHit<Item> searchHit : list) {
            System.out.println(searchHit.getContent());
        }
    }

    @Test
    void searchRangeQuery() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -15);
        RangeQueryBuilder queryBuilder = QueryBuilders.rangeQuery("@timestamp").gte(calendar.getTime());
        Query query = new NativeSearchQuery(queryBuilder);

        SearchHits<Item> searchHits = elasticsearchRestTemplate.search(query, Item.class);

        List<SearchHit<Item>> list = searchHits.getSearchHits();

        for (SearchHit<Item> searchHit : list) {
            System.out.println(searchHit.getContent());
        }
    }

    /**
     * range query
     * 范围查询
     */
    @Test
    void searchBoolQuery() {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        List<QueryBuilder> conditions = new ArrayList<>();
        conditions.add(QueryBuilders.rangeQuery("price").gte(500000).lte(900000));
        conditions.add(QueryBuilders.matchQuery("title", "手机"));
        queryBuilder.must().addAll(conditions);

        Query query = new NativeSearchQuery(queryBuilder);
        SearchHits<Item> searchHits = elasticsearchRestTemplate.search(query, Item.class);

        List<SearchHit<Item>> list = searchHits.getSearchHits();

        for (SearchHit<Item> searchHit : list) {
            System.out.println(searchHit.getContent());
        }
    }

    @Test
    void searchPageAndSort() {
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("title", "高端智能");
        Query query = new NativeSearchQuery(queryBuilder);

        // 排序
        query.addSort(Sort.by(Sort.Direction.ASC, "price"));

        // 分页，第一页从 0 开始
        query.setPageable(PageRequest.of(0, 2));

        System.out.println("第一页：");
        SearchHits<Item> searchHits = elasticsearchRestTemplate.search(query, Item.class);
        List<SearchHit<Item>> list = searchHits.getSearchHits();
        for (SearchHit<Item> searchHit : list) {
            System.out.println(searchHit.getContent());
        }

        System.out.println("第二页：");
        query.setPageable(PageRequest.of(1, 2));
        searchHits = elasticsearchRestTemplate.search(query, Item.class);
        list = searchHits.getSearchHits();
        for (SearchHit<Item> searchHit : list) {
            System.out.println(searchHit.getContent());
        }
    }

    /**
     * 高亮查询
     */
    @Test
    void searchHighlightQuery() {
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("title")
                .preTags("<span style='color:red'>")
                .postTags("</span>");

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("title", "高端手机"))
                .withHighlightBuilder(highlightBuilder)
                .build();

        SearchHits<Item> searchHits = elasticsearchRestTemplate.search(searchQuery, Item.class);

        List<SearchHit<Item>> list = searchHits.getSearchHits();

        for (SearchHit<Item> searchHit : list) {
            Item item = searchHit.getContent();
            System.out.println("原始数据：" + item);
            List<String> fields = searchHit.getHighlightField("title");
            if (list.size() > 0) {
                String highlightTitle = fields.get(0);
                item.setTitle(highlightTitle);
                System.out.println("高亮数据：" + item);
            }

        }
    }
}
