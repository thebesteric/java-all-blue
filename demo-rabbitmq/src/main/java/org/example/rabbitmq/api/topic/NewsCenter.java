package org.example.rabbitmq.api.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.example.rabbitmq.api.utils.RabbitConstant;
import org.example.rabbitmq.api.utils.RabbitUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class NewsCenter {
    public static void main(String[] args) throws Exception {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();


        Map<String, String> messages = new HashMap<>();
        messages.put("sport.hefei.2021-11-21", "合肥体育");
        messages.put("article.hefei.2021-11-22", "合肥文章");
        messages.put("sport.shanghai.2021-11-21", "上海体育");
        messages.put("article.shanghai.2021-11-22", "上海文章");

        messages.forEach((routingKey, message) -> {
            try {
                channel.basicPublish(RabbitConstant.EXCHANGE_NEWS_TOPIC, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        channel.close();
        connection.close();
    }
}
