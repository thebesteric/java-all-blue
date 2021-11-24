package org.example.rabbitmq.boot.confirm;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class MessageConfirm implements RabbitTemplate.ConfirmCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            // 接收成功
            System.out.println("Confirm：消息发送到交换机【成功】：" + cause);
        } else {
            // 接收成功
            System.out.println("Confirm：消息发送到交换机【失败】：" + cause);
        }
    }
}
