package org.example.rabbitmq.boot.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * topic 模式,需要定义 routingKey
 * 需要定义交换机，定义多个队列，每个队列按 routingKey 正则，获取所需要到数据
 */
@Configuration
public class DirectQueueConfig {

    public static final String EXCHANGE_NAME = "direct.exchange";
    public static final String QUEUE_NAME_HEFEI = "direct.queue.hefei";
    public static final String QUEUE_NAME_SHANGHAI = "direct.queue.shanghai";
    public static final String ROUTING_KRY_QUEUE_HEFEI = "routing.key.hefei";
    public static final String ROUTING_KRY_QUEUE_SHANGHAI = "routing.key.shanghai";

    // 声明队列
    @Bean
    public Queue directQueueHefei() {
        return new Queue(QUEUE_NAME_HEFEI);
    }

    @Bean
    public Queue directQueueShanghai() {
        return new Queue(QUEUE_NAME_SHANGHAI);
    }

    // 声明交换机
    @Bean
    public DirectExchange directExchange() {
       return new DirectExchange(EXCHANGE_NAME);
    }

    // 绑定队列与交换机到关系
    @Bean
    public Binding bindDirectQueueHefei(Queue directQueueHefei, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueueHefei).to(directExchange).with(ROUTING_KRY_QUEUE_HEFEI);
    }

    @Bean
    public Binding bindDirectQueueShanghai(Queue directQueueShanghai, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueueShanghai).to(directExchange).with(ROUTING_KRY_QUEUE_SHANGHAI);
    }
}
