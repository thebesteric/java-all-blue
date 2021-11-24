package org.example.rabbitmq.boot.listener;

import com.rabbitmq.client.Channel;
import org.example.rabbitmq.boot.config.FanoutQueueConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FanoutQueueListener {

    @RabbitListener(queues = FanoutQueueConfig.QUEUE_NAME_1)
    public void listener1(Message message, Channel channel) throws IOException {
        System.out.println("【Fanout-Queue-1】" + message);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(deliveryTag, true);
    }

    @RabbitListener(queues = FanoutQueueConfig.QUEUE_NAME_2)
    public void listener2(Message message, Channel channel) throws IOException {
        System.out.println("【Fanout-Queue-2】" + message);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(deliveryTag, true);
    }
}
