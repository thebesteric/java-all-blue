package org.example.rabbitmq.boot.listener;

import com.rabbitmq.client.Channel;
import org.example.rabbitmq.boot.config.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Random;

@Component
public class MessageListener {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void listener(Message message, Channel channel) throws IOException {
        // 1、获取消息的 ID
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        // 2、获取消息
        System.out.println("message = " + new String(message.getBody()));
        // 3、业务处理
        try {
            int number = new Random().nextInt(2);
            System.out.println("处理业务..." + number);
            // int i = 1 / number;
            // 4、确认消息
            channel.basicAck(deliveryTag, false);
            System.out.println("消息确认【成功】");
        } catch (Exception ex) {
            // 4、拒绝签收
            channel.basicNack(deliveryTag, false, true);
            System.out.println("消息确认【失败】重新返回队列");
        }
    }
}
