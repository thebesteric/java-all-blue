package org.example.rabbitmq.api.helloword;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.example.rabbitmq.api.utils.RabbitConstant;
import org.example.rabbitmq.api.utils.RabbitUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取 TCP 长连接
        Connection connection = RabbitUtils.getConnection();
        // 创建连接通道，相当于 TCP 中的虚拟连接
        Channel channel = connection.createChannel();

        // 创建队列
        // 参数1：队列名称
        // 参数2：是否消息持久化
        // 参数3：是否队列私有化，false：表示所有消费者都可以访问，true：表示第一次拥有他都消费者才能使用
        // 参数4：是否自动删除，false：连接停止后不自动删除这个队列
        // 参数5：其他配置
        channel.queueDeclare(RabbitConstant.QUEUE_HELLO_WORLD, false, false, false, null);

        String message = "helloWorld-" + (System.currentTimeMillis() / 1000);

        // 发送消息
        // 参数1：交换机名称
        // 参数2：队列名称
        // 参数3：额外信息
        // 参数4：消息的字节数组
        channel.basicPublish("", RabbitConstant.QUEUE_HELLO_WORLD, null, message.getBytes(StandardCharsets.UTF_8));

        channel.close();
        connection.close();

        System.out.println("发送消息：" + message);
    }
}
