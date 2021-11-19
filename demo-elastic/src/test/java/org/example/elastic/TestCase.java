package org.example.elastic;

import org.example.elastic.api.entity.JobDetail;
import org.example.elastic.api.service.JobFullTextService;
import org.example.elastic.api.service.impl.JobFullTextServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestCase {

    private JobFullTextService jobFullTextService;

    @Before
    public void beforeTest() {
        jobFullTextService = new JobFullTextServiceImpl();
    }

    @After
    public void afterTest() throws IOException {
        jobFullTextService.close();
    }

    @Test
    public void add() throws IOException {
        JobDetail jobDetail = new JobDetail();
        jobDetail.setId(1);
        jobDetail.setArea("北京");
        jobDetail.setCmp("清华大学");
        jobDetail.setEdu("本科及以上");
        jobDetail.setExp("五年工作经验以上");
        jobDetail.setTitle("Java架构师");
        jobDetail.setJobType("全职");
        jobDetail.setPv(3000);
        jobDetail.setJd("Java架构");
        jobDetail.setSalary("60K/月");
        jobDetail.setAge(30);
        jobFullTextService.add(jobDetail);

        jobDetail.setId(2);
        jobDetail.setArea("上海");
        jobDetail.setCmp("交通大学");
        jobDetail.setEdu("本科及以上");
        jobDetail.setExp("三年工作经验以上");
        jobDetail.setTitle("大数据工程师");
        jobDetail.setJobType("全职");
        jobDetail.setPv(3500);
        jobDetail.setJd("大数据");
        jobDetail.setSalary("面议");
        jobDetail.setAge(35);
        // jobFullTextService.add(jobDetail);

        jobDetail.setId(3);
        jobDetail.setArea("上海");
        jobDetail.setCmp("上海复旦大学");
        jobDetail.setEdu("本科及以上");
        jobDetail.setExp("三年工作经验以上");
        jobDetail.setTitle("Php架构师");
        jobDetail.setJobType("全职");
        jobDetail.setPv(2500);
        jobDetail.setJd("Php");
        jobDetail.setSalary("40K/月");
        jobDetail.setAge(30);
        // jobFullTextService.add(jobDetail);
    }

    @Test
    public void getById() throws IOException {
        System.out.println(jobFullTextService.getById(1));
    }

    @Test
    public void update() throws IOException {
        JobDetail jobDetail = jobFullTextService.getById(1);
        jobDetail.setTitle("Java高级架构师");
        jobDetail.setSalary("面议");
        jobFullTextService.update(jobDetail);
    }

    @Test
    public void delete() throws IOException {
        jobFullTextService.deleteById(1);
    }

    @Test
    public void searchByKeywords() throws IOException {
        List<JobDetail> jobDetails = jobFullTextService.searchByKeywords("Java架构");
        for (JobDetail jobDetail : jobDetails) {
            System.out.println(jobDetail);
        }
    }

    @Test
    public void searchByPage() throws IOException {
        Map<String, Object> result = jobFullTextService.searchByPage("Java架构", 1, 1);
        System.out.println("total = " + result.get("total"));
        for (JobDetail jobDetail : (List<JobDetail>) result.get("data")) {
            System.out.println("record = " + jobDetail);
        }
    }

    @Test
    public void searchByScrollPage() throws IOException {
        Map<String, Object> result = jobFullTextService.searchByScrollPage("Java架构", null, 1);
        System.out.println("total = " + result.get("total"));
        System.out.println("scrollId = " + result.get("scrollId"));
        for (JobDetail jobDetail : (List<JobDetail>) result.get("data")) {
            System.out.println("record = " + jobDetail);
        }
    }

}
