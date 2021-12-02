package org.example.dubbo.simulate.framework.protocol.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.dubbo.simulate.framework.Invocation;

import java.io.IOException;

public class HttpClient {

    public String send(String hostname, Integer port, Invocation invocation) {
        String result = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpPost request = new HttpPost(hostname+ ":" + port);
            request.setEntity(new StringEntity(JSONObject.toJSONString(invocation)));

            CloseableHttpResponse response = httpClient.execute(request);
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
