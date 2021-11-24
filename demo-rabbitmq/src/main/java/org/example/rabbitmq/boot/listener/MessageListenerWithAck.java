package org.example.rabbitmq.boot.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import java.util.Random;

// @Component
public class MessageListenerWithAck implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        // 1、获取消息的 ID
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        // 2、获取消息
        System.out.println("message = " + new String(message.getBody()));
        // 3、业务处理
        try {
            int number = new Random().nextInt(2);
            System.out.println("处理业务..." + number);
            int i = 1 / number;
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
