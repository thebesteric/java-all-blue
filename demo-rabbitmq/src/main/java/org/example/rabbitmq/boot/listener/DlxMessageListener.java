package org.example.rabbitmq.boot.listener;

import com.rabbitmq.client.Channel;
import org.example.rabbitmq.boot.config.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DlxMessageListener {
    @RabbitListener(queues = RabbitMQConfig.QUEUE_DLX)
    public void listener(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("接收到死信 = " + new String(message.getBody()));

        System.out.println("====================");
        System.out.println("处理业务逻辑...");
        System.out.println("根据订单ID查询状态...");
        System.out.println("判断订单是否支付成功...未支付");
        System.out.println("取消订单，回滚库存...");
        System.out.println("====================");

        channel.basicAck(deliveryTag, true);
    }
}
