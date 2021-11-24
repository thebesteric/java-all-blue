package org.example.rabbitmq.api.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitUtils {

    private static final ConnectionFactory connectionFactory = new ConnectionFactory();
    static {
        connectionFactory.setHost(RabbitConstant.HOST);
        connectionFactory.setPort(RabbitConstant.PORT);
        connectionFactory.setVirtualHost(RabbitConstant.VIRTUAL_HOST);
        connectionFactory.setUsername(RabbitConstant.USERNAME);
        connectionFactory.setPassword(RabbitConstant.PASSWORD);
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return connection;
    }

}
