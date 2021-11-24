package org.example.rabbitmq.boot.listener;

import com.rabbitmq.client.Channel;
import org.example.rabbitmq.boot.config.WorkQueueConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WorkQueueListener {

    @RabbitListener(queues = WorkQueueConfig.QUEUE_NAME)
    public void listener1(Message message, Channel channel) throws IOException {
        System.out.println("【Work-Queue-1】" + message);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(deliveryTag, true);
    }

    @RabbitListener(queues = WorkQueueConfig.QUEUE_NAME)
    public void listener2(Message message, Channel channel) throws IOException {
        System.out.println("【Work-Queue-2】" + message);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(deliveryTag, true);
    }
}
