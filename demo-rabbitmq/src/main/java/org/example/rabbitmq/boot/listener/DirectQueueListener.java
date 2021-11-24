package org.example.rabbitmq.boot.listener;

import com.rabbitmq.client.Channel;
import org.example.rabbitmq.boot.config.DirectQueueConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DirectQueueListener {

    @RabbitListener(queues = DirectQueueConfig.QUEUE_NAME_HEFEI)
    public void listenerHefei(Message message, Channel channel) throws IOException {
        System.out.println("【Direct-Hefei-Queue】" + message);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(deliveryTag, true);
    }

    @RabbitListener(queues = DirectQueueConfig.QUEUE_NAME_SHANGHAI)
    public void listenerShanghai(Message message, Channel channel) throws IOException {
        System.out.println("【Direct-Shanghai-Queue】" + message);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(deliveryTag, true);
    }
}
