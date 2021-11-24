package org.example.rabbitmq.boot.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * topic 模式,需要定义 routingKey
 * 需要定义交换机，定义多个队列，每个队列按 routingKey 正则，获取所需要到数据
 */
@Configuration
public class TopicQueueConfig {

    public static final String EXCHANGE_NAME = "topic.exchange";
    public static final String QUEUE_NAME_HEFEI = "topic.queue.hefei";
    public static final String QUEUE_NAME_SHANGHAI = "topic.queue.shanghai";
    public static final String ROUTING_KRY_QUEUE_HEFEI = "routing.key.hefei.#";
    public static final String ROUTING_KRY_QUEUE_SHANGHAI = "routing.key.shanghai.#";

    // 声明队列
    @Bean
    public Queue topicQueueHefei() {
        return new Queue(QUEUE_NAME_HEFEI);
    }

    @Bean
    public Queue topicQueueShanghai() {
        return new Queue(QUEUE_NAME_SHANGHAI);
    }

    // 声明交换机
    @Bean
    public TopicExchange topicExchange() {
       return new TopicExchange(EXCHANGE_NAME);
    }

    // 绑定队列与交换机到关系
    @Bean
    public Binding bindTopicQueueHefei(Queue topicQueueHefei, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueueHefei).to(topicExchange).with(ROUTING_KRY_QUEUE_HEFEI);
    }

    @Bean
    public Binding bindTopicQueueShanghai(Queue topicQueueShanghai, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueueShanghai).to(topicExchange).with(ROUTING_KRY_QUEUE_SHANGHAI);
    }
}
