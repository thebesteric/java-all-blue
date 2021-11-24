package org.example.rabbitmq.api.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.example.rabbitmq.api.utils.RabbitConstant;
import org.example.rabbitmq.api.utils.RabbitUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class LogSystem {
    public static void main(String[] args) throws Exception {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        while (true) {
            // error, info, warn, debug
            String level = new Scanner(System.in).nextLine();
            String message = new Scanner(System.in).nextLine();
            // routingKey = error, info, warn, debug
            channel.basicPublish(RabbitConstant.EXCHANGE_LOG_ROUTING, level, null, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
