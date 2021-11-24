package org.example.rabbitmq.api.topic;

import com.rabbitmq.client.*;
import org.example.rabbitmq.api.utils.RabbitConstant;
import org.example.rabbitmq.api.utils.RabbitUtils;

import java.io.IOException;

public class ShanghaiNews {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(RabbitConstant.QUEUE_NEWS_SHANGHAI, false, false, false, null);

        // 队列绑定交换机
        // 参数1：队列名称
        // 参数2：绑定到到交换机名称
        // 参数3：路由 key
        // #：表示一个或多个词
        // *：表示匹配一个词
        channel.queueBind(RabbitConstant.QUEUE_NEWS_SHANGHAI, RabbitConstant.EXCHANGE_NEWS_TOPIC, "*.shanghai.#");
        channel.basicQos(1);

        channel.basicConsume(RabbitConstant.QUEUE_NEWS_SHANGHAI, false, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("【上海新闻】收到：" + new String(body));
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
