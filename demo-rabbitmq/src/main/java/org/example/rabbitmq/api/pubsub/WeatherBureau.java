package org.example.rabbitmq.api.pubsub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.example.rabbitmq.api.utils.RabbitConstant;
import org.example.rabbitmq.api.utils.RabbitUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class WeatherBureau {
    public static void main(String[] args) throws Exception {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        while (true) {
            String input = new Scanner(System.in).nextLine();
            if ("bye".equals(input)) {
                break;
            }
            channel.basicPublish(RabbitConstant.EXCHANGE_WEATHER, "", null, input.getBytes(StandardCharsets.UTF_8));
        }
        channel.close();
        connection.close();
    }
}
