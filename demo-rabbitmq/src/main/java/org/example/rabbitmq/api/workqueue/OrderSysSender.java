package org.example.rabbitmq.api.workqueue;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.example.rabbitmq.api.utils.RabbitConstant;
import org.example.rabbitmq.api.utils.RabbitUtils;

import java.nio.charset.StandardCharsets;

public class OrderSysSender {

    private static final Gson gson = new Gson();

    public static void main(String[] args) throws Exception {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();

        for (int i = 1; i <= 100; i++) {
            SMS sms = new SMS("用户" + i, "1300000000" + i, "您的车票订购成功");
            String json = gson.toJson(sms);
            channel.basicPublish("", RabbitConstant.QUEUE_SMS, null, json.getBytes(StandardCharsets.UTF_8));
        }

        System.out.println("短信发送成功");
        channel.close();
        connection.close();
    }
}
