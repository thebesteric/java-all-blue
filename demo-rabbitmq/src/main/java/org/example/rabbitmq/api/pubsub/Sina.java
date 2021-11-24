package org.example.rabbitmq.api.pubsub;

import com.rabbitmq.client.*;
import org.example.rabbitmq.api.utils.RabbitConstant;
import org.example.rabbitmq.api.utils.RabbitUtils;

import java.io.IOException;

public class Sina {
    public static void main(String[] args) throws Exception {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(RabbitConstant.QUEUE_WEATHER_SINA, false, false, false, null);

        // 队列绑定交换机
        // 参数1：队列名称
        // 参数2：绑定到到交换机名称
        // 参数3：路由 key
        channel.queueBind(RabbitConstant.QUEUE_WEATHER_SINA, RabbitConstant.EXCHANGE_WEATHER, "");
        channel.basicQos(1);

        channel.basicConsume(RabbitConstant.QUEUE_WEATHER_SINA, false, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("【新浪天气】收到：" + new String(body));
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
