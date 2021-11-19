package org.example.elastic.api.service;

import org.example.elastic.api.entity.JobDetail;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface JobFullTextService {
    void add(JobDetail jobDetail) throws IOException;

    JobDetail getById(int id) throws IOException;

    void update(JobDetail jobDetail) throws IOException;

    void deleteById(int id) throws IOException;

    List<JobDetail> searchByKeywords(String keywords) throws IOException;

    Map<String, Object> searchByPage(String keywords, int pageNo, int pageSize) throws IOException;

    Map<String, Object> searchByScrollPage(String keywords, String scrollId, int pageSize) throws IOException;

    void close() throws IOException;
}
