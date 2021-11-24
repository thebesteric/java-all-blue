package org.example.rabbitmq.boot.confirm;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class MessageReturn implements RabbitTemplate.ReturnCallback {
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        System.out.println("Return：消息发送到队列【失败】" + message);
    }
}
