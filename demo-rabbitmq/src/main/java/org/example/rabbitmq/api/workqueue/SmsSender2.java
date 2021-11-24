package org.example.rabbitmq.api.workqueue;

import com.rabbitmq.client.*;
import org.example.rabbitmq.api.utils.RabbitConstant;
import org.example.rabbitmq.api.utils.RabbitUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SmsSender2 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();

        // 创建或指定队列
        // 参数1：队列名称
        // 参数2：是否消息持久化
        // 参数3：是否队列私有化，false：表示所有消费者都可以访问，true：表示第一次拥有他都消费者才能使用
        // 参数4：是否自动删除，false：连接停止后不自动删除这个队列
        // 参数5：其他配置
        channel.queueDeclare(RabbitConstant.QUEUE_SMS, false, false, false, null);

        // 如果不写 channel.basicQos(1); 则 MQ 会自动讲所有消息轮询的方式一次性分配给所有消费者
        // 不再对消费者一次发送多个请求，而是消费者处理完消息后（确认后），主动从队列中获取下一个
        // 处理完一个取一个
        channel.basicQos(1);

        // 从 MQ 服务器中获取数据
        // 参数1：队列名称
        // 参数2：是否自动确认消息，false（推荐）：表示手动确认消息，true：表示自动确认消息
        // 参数3：消费者，为 DefaultConsumer 的实现类
        channel.basicConsume(RabbitConstant.QUEUE_SMS, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String jsonSms = new String(body);
                System.out.println("SMS_Sender_2 短发发送成功" + jsonSms);
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
