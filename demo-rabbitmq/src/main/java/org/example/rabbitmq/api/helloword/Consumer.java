package org.example.rabbitmq.api.helloword;

import com.rabbitmq.client.*;
import org.example.rabbitmq.api.utils.RabbitConstant;
import org.example.rabbitmq.api.utils.RabbitUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取 TCP 长连接
        Connection connection = RabbitUtils.getConnection();
        // 创建连接通道，相当于 TCP 中的虚拟连接
        Channel channel = connection.createChannel();

        // 创建或指定队列
        // 参数1：队列名称
        // 参数2：是否消息持久化
        // 参数3：是否队列私有化，false：表示所有消费者都可以访问，true：表示第一次拥有他都消费者才能使用
        // 参数4：是否自动删除，false：连接停止后不自动删除这个队列
        // 参数5：其他配置
        channel.queueDeclare(RabbitConstant.QUEUE_HELLO_WORLD, false, false, false, null);

        // 从 MQ 服务器中获取数据
        // 参数1：队列名称
        // 参数2：是否自动确认消息，false（推荐）：表示手动确认消息，true：表示自动确认消息
        // 参数3：消费者，为 DefaultConsumer 的实现类
        channel.basicConsume(RabbitConstant.QUEUE_HELLO_WORLD, false, new Receiver(channel));

        System.out.println("等待接受消息...");
    }

    static class Receiver extends DefaultConsumer {

        private Channel channel;

        public Receiver(Channel channel) {
            super(channel);
            this.channel = channel;
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            String message = new String(body);
            System.out.println("============begin==============");
            System.out.println("消费者接受到消息 = " + message);
            System.out.println("消息TagID = " + envelope.getDeliveryTag());
            System.out.println("============end==============");
            System.out.println();

            // 确认消息，false：表示仅确认当前消息，true：表示同时签收之前未签收到所有消息
            channel.basicAck(envelope.getDeliveryTag(), false);
        }
    }
}
