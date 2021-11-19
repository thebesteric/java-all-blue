package org.example.elastic.api.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.example.elastic.api.entity.JobDetail;
import org.example.elastic.api.service.JobFullTextService;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobFullTextServiceImpl implements JobFullTextService {

    ObjectMapper objectMapper = new ObjectMapper();

    private final RestHighLevelClient restHighLevelClient;

    private static final String JOB_INDEX = "job_index";

    public JobFullTextServiceImpl() {
        // 使用 HttpHost 添加 ES 节点
        RestClientBuilder builder = RestClient.builder(
                new HttpHost("127.0.0.1", 9200, "http"));
        restHighLevelClient = new RestHighLevelClient(builder);
    }

    @Override
    public void add(JobDetail jobDetail) throws IOException {
        // 1、构建 IndexRequest 对象，用来描述 ES 发起请求的数据
        IndexRequest indexRequest = new IndexRequest(JOB_INDEX);
        // 2、设置文档 ID
        indexRequest.id(jobDetail.getId() + "");
        // 3、将对象转换为 json
        String json = objectMapper.writeValueAsString(jobDetail);
        // 4、设置文档数据，并指定 json 格式
        indexRequest.source(json, XContentType.JSON);
        // 5、发起请求到 ES
        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    @Override
    public JobDetail getById(int id) throws IOException {
        // 1、构建 GetRequest 请求
        GetRequest getRequest = new GetRequest(JOB_INDEX, id + "");
        // 2、发送 Get 请求
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        // 3、将响应转为 json 格式字符串
        String sourceAsString = getResponse.getSourceAsString();
        // 4、转换为对象
        JobDetail jobDetail = objectMapper.readValue(sourceAsString, JobDetail.class);
        // 5、设置 ID，因为 ES 不回在 source 中存储 ID
        jobDetail.setId(id);
        return jobDetail;
    }

    @Override
    public void update(JobDetail jobDetail) throws IOException {
        // 1、构建查询请求
        GetRequest getRequest = new GetRequest(JOB_INDEX, jobDetail.getId() + "");
        // 2、查询是否存在
        boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        if (exists) {
            // 3、构建 UpdateRequest 请求
            UpdateRequest updateRequest = new UpdateRequest(JOB_INDEX, jobDetail.getId() + "");
            // 4、设置 doc
            updateRequest.doc(objectMapper.writeValueAsString(jobDetail), XContentType.JSON);
            // 5、发送更新请求
            restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        }
    }

    @Override
    public void deleteById(int id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(JOB_INDEX, id + "");
        restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    @Override
    public List<JobDetail> searchByKeywords(String keywords) throws IOException {
        // 1、构建检索请求
        SearchRequest searchRequest = new SearchRequest(JOB_INDEX);
        // 2、创建一个 SearchSourceBuilder 用来构建查询条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 3、使用 MultiMatchQueryBuilder 构建查询条件
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keywords, "title", "jd");
        // 4、将查询条件设置到构建起中
        searchSourceBuilder.query(multiMatchQueryBuilder);
        // 5、调用 searchRequest.source 将查询条件设置进去
        searchRequest.source(searchSourceBuilder);
        // 6、发起请求
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // 7、命中结果
        SearchHit[] hits = searchResponse.getHits().getHits();

        List<JobDetail> jobDetails = new ArrayList<>();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            JobDetail jobDetail = objectMapper.readValue(sourceAsString, JobDetail.class);
            jobDetail.setId(Integer.parseInt(hit.getId()));
            jobDetails.add(jobDetail);
        }

        return jobDetails;
    }

    @Override
    public Map<String, Object> searchByPage(String keywords, int pageNo, int pageSize) throws IOException {
        SearchRequest searchRequest = new SearchRequest(JOB_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keywords, "title", "jd");
        searchSourceBuilder.query(multiMatchQueryBuilder);
        // 每页显示多少条
        searchSourceBuilder.size(pageSize);
        // 设置从第几条开始
        searchSourceBuilder.from((pageNo - 1) * pageSize);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();

        List<JobDetail> jobDetails = new ArrayList<>();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            JobDetail jobDetail = objectMapper.readValue(sourceAsString, JobDetail.class);
            jobDetail.setId(Integer.parseInt(hit.getId()));
            jobDetails.add(jobDetail);
        }

        Map<String, Object> result = new HashMap<>();
        long total = searchResponse.getHits().getTotalHits().value;
        result.put("total", total);
        result.put("data", jobDetails);
        return result;
    }

    @Override
    public Map<String, Object> searchByScrollPage(String keywords, String scrollId, int pageSize) throws IOException {
        SearchResponse searchResponse;
        if (StringUtils.isEmpty(scrollId)) {
            SearchRequest searchRequest = new SearchRequest(JOB_INDEX);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keywords, "title", "jd");
            searchSourceBuilder.query(multiMatchQueryBuilder);

            // 每页显示多少条
            searchSourceBuilder.size(pageSize);
            // 设置从第几条开始
            searchSourceBuilder.from(0);

            // === 设置高亮 ===
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("title");
            highlightBuilder.field("jd");
            highlightBuilder.preTags("<font color=\"red\">");
            highlightBuilder.postTags("</font>");

            // 给请求设置高亮
            searchSourceBuilder.highlighter(highlightBuilder);

            // 封装查询条件到 source
            searchRequest.source(searchSourceBuilder);

            // 设置 scroll 查询，有效期 1 分钟
            searchRequest.scroll(TimeValue.timeValueMinutes(1));

            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        }
        // 第二次请求，会带有 scrollId
        else {
            SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId);
            searchScrollRequest.scroll(TimeValue.timeValueMinutes(5));

            // 发送 scroll 请求
            searchResponse = restHighLevelClient.scroll(searchScrollRequest, RequestOptions.DEFAULT);
        }

        // 封装数据
        SearchHit[] hits = searchResponse.getHits().getHits();

        List<JobDetail> jobDetails = new ArrayList<>();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            JobDetail jobDetail = objectMapper.readValue(sourceAsString, JobDetail.class);
            jobDetail.setId(Integer.parseInt(hit.getId()));

            // 设置高亮
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField titleHL = highlightFields.get("title");
            HighlightField jdHL = highlightFields.get("jd");

            if (titleHL != null) {
                // 获取高亮片段
                Text[] fragments = titleHL.getFragments();
                StringBuilder builder = new StringBuilder();
                for (Text fragment : fragments) {
                    builder.append(fragment);
                }
                // 替换为高亮
                jobDetail.setTitle(builder.toString());
            }

            if (jdHL != null) {
                // 获取高亮片段
                Text[] fragments = jdHL.getFragments();
                StringBuilder builder = new StringBuilder();
                for (Text fragment : fragments) {
                    builder.append(fragment);
                }
                // 替换为高亮
                jobDetail.setJd(builder.toString());
            }

            jobDetails.add(jobDetail);
        }

        Map<String, Object> result = new HashMap<>();
        long total = searchResponse.getHits().getTotalHits().value;
        result.put("total", total);
        result.put("scrollId", searchResponse.getScrollId());
        result.put("data", jobDetails);
        return result;
    }

    @Override
    public void close() throws IOException {
        restHighLevelClient.close();
    }
}
