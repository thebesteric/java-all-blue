package org.example.springboot.controller;

import io.github.thebesteric.framework.apm.agent.commons.constant.Level;
import io.github.thebesteric.framework.apm.agent.extension.annotation.ApmTracing;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.example.springboot.feign.BaiduFeign;
import org.example.springboot.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * HelloController
 *
 * @author wangweijun
 * @version v1.0
 * @since 2023-09-14 12:05:15
 */
@RestController
@ApmTracing(level = Level.WARN, extra = "test")
public class HelloController {

    @Resource
    private HelloService helloService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BaiduFeign baiduFeign;

    @ApmTracing(tag = "hello", extra = "hello-extra")
    @GetMapping("/hello")
    public String hello(String name) throws InterruptedException {
        Thread.sleep(1000);

        new Thread(() -> helloService.hello("in-thread")).start();

        return helloService.hello(name);
    }

    @ApmTracing
    @PostMapping("/upload")
    public String upload(MultipartFile file) {
        return file.getOriginalFilename();
    }

    @ApmTracing
    @GetMapping("/rest-template")
    public Object restTemplate() throws URISyntaxException {
        String object = restTemplate.getForObject(new URI("https://www.baidu.com"), String.class);
        return object;
    }

    @ApmTracing
    @GetMapping("/feign")
    public Object feign() {
        String object = baiduFeign.baidu();
        return object;
    }

    @ApmTracing
    @GetMapping("/http-client")
    public Object httpClient() {
        //获得http客户端
        CloseableHttpClient client = HttpClientBuilder.create().build();

        //参数
        StringBuilder params = new StringBuilder();

        try{
            params.append("name=").append(URLEncoder.encode("&","utf-8")).append("&").append("age=24");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //创建Get请求
        HttpGet httpGet = new HttpGet("https://www.baidu.com/?" + params);
        //响应模型
        CloseableHttpResponse response = null;
        try{
            //配置信息
            RequestConfig config = RequestConfig.custom()
                    //设置连接超时时间
                    .setConnectTimeout(5000)
                    //设置请求超时时间
                    .setConnectionRequestTimeout(5000)
                    //socket读写超时时间
                    .setSocketTimeout(5000)
                    //设置是否允许重定向(默认为true)
                    .setRedirectsEnabled(true)
                    .build();
            //将上面的配置信息配入Get请求中
            httpGet.setConfig(config);

            //由客户端执行Get请求
            response = client.execute(httpGet);

            //从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应状态为:"+response.getStatusLine());
            if (responseEntity!=null){
                System.out.println("响应内容长度为:"+responseEntity.getContentLength());
                return EntityUtils.toString(responseEntity,"utf-8");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(client!=null){
                    client.close();
                }
                if (response!=null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
