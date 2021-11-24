package org.example.rabbitmq.boot.controller;

import org.example.rabbitmq.boot.config.*;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/rabbit")
public class RabbitMQController {

    @Autowired
    public RabbitTemplate rabbitTemplate;

    @GetMapping("/send")
    public Object send(String message) throws InterruptedException {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, message);
        return "success";
    }

    @GetMapping("/work/send")
    public Object sendToWorkQueue(String message) throws InterruptedException {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        rabbitTemplate.send(WorkQueueConfig.QUEUE_NAME, new Message(message.getBytes(StandardCharsets.UTF_8), messageProperties));
        // rabbitTemplate.convertAndSend(WorkQueueConfig.QUEUE_NAME, message);
        return "success";
    }

    @GetMapping("/fanout/send")
    public Object sendToFanoutQueue(String message) throws InterruptedException {
        rabbitTemplate.convertAndSend(FanoutQueueConfig.EXCHANGE_NAME, "", message);
        return "success";
    }

    @GetMapping("/topic/send")
    public Object sendToTopicQueue(String message, String routingKey) throws InterruptedException {
        rabbitTemplate.convertAndSend(TopicQueueConfig.EXCHANGE_NAME, routingKey, message);
        return "success";
    }

    @GetMapping("/direct/send")
    public Object sendToDirectQueue(String message, String routingKey) throws InterruptedException {
        rabbitTemplate.convertAndSend(DirectQueueConfig.EXCHANGE_NAME, routingKey, message);
        return "success";
    }

}
