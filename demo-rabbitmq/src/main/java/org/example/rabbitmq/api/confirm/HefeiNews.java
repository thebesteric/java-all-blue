package org.example.rabbitmq.api.confirm;

import com.rabbitmq.client.*;
import org.example.rabbitmq.api.utils.RabbitConstant;
import org.example.rabbitmq.api.utils.RabbitUtils;

import java.io.IOException;

public class HefeiNews {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(RabbitConstant.QUEUE_NEWS_HEFEI, false, false, false, null);

        // 队列绑定交换机
        // 参数1：队列名称
        // 参数2：绑定到到交换机名称
        // 参数3：路由 key
        channel.queueBind(RabbitConstant.QUEUE_NEWS_HEFEI, RabbitConstant.EXCHANGE_NEWS_TOPIC, "*.hefei.#");
        channel.basicQos(1);

        channel.basicConsume(RabbitConstant.QUEUE_NEWS_HEFEI, false, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("【合肥新闻】收到：" + new String(body));
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
