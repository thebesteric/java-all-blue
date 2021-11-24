package org.example.rabbitmq.boot.test;

import org.example.rabbitmq.boot.config.RabbitMQConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitMQTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void send() throws InterruptedException {
        String message = "boot.mq.test." + (System.currentTimeMillis() / 1000);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, message);
        // rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "TEST." + RabbitMQConfig.ROUTING_KEY, message);

        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void sendMultiple() throws InterruptedException {

        for (int i = 0; i < 10; i++) {
            String message = "boot.mq.test." + i;
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, message);
        }

        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void testDlx() throws InterruptedException {
        // 发送延迟死信
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TTL, "ttl.order", "{\"orderId\": \"123456\"}");

        TimeUnit.SECONDS.sleep(20);
    }

}
