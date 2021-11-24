package org.example.rabbitmq.api.confirm;

import com.rabbitmq.client.*;
import org.example.rabbitmq.api.utils.RabbitConstant;
import org.example.rabbitmq.api.utils.RabbitUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class NewsCenter {
    public static void main(String[] args) throws Exception {

        Map<String, String> messages = new HashMap<>();
        messages.put("sport.hefei.2021-11-21", "合肥体育");
        messages.put("article.hefei.2021-11-22", "合肥文章");
        messages.put("sport.shanghai.2021-11-21", "上海体育");
        messages.put("article.shanghai.2021-11-22", "上海文章");

        messages.put("sport.usa.2021-11-22", "美国体育");
        messages.put("article.usa.2021-11-22", "美国文章");

        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();

        // 开启 confirm 监听模式
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("消息已经被 broker 接受， Tag = " + deliveryTag);
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("消息已经被 broker 拒绝， Tag = " + deliveryTag);
            }
        });

        // 如果消息没有队列可以接收，则返回到该方法
        channel.addReturnListener(new ReturnCallback() {
            @Override
            public void handle(Return returnMessage) {
                System.err.println("====================");;
                System.err.println("Return 编码："+ returnMessage.getReplyCode());;
                System.err.println("Return 描述："+ returnMessage.getReplyText());;
                System.err.println("交换机："+ returnMessage.getExchange());;
                System.err.println("路由 key："+ returnMessage.getRoutingKey());;
                System.err.println("消息："+ new String(returnMessage.getBody()));;
                System.err.println("====================");;
            }
        });

        messages.forEach((routingKey, message) -> {
            try {
                // 第三个参数：mandatory：true 代表如果消息无法正常投递到队列，则 return 回生产者，false（默认）：代表直接丢弃
                channel.basicPublish(RabbitConstant.EXCHANGE_NEWS_TOPIC, routingKey, true,null, message.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // channel.close();
        // connection.close();
    }
}
