package org.example.rabbitmq.boot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * pub/sub 模式（广播模式）,不需要 routingKey
 * 需要定义交换机，定义一个队列，所有消费者获取到到信息都相同
 */
@Configuration
public class FanoutQueueConfig {

    public static final String EXCHANGE_NAME = "fanout.exchange";
    public static final String QUEUE_NAME_1 = "fanout.queue.1";
    public static final String QUEUE_NAME_2 = "fanout.queue.2";

    // 声明队列
    @Bean
    public Queue fanoutQueue1() {
        return new Queue(QUEUE_NAME_1);
    }

    @Bean
    public Queue fanoutQueue2() {
        return new Queue(QUEUE_NAME_2);
    }

    // 声明交换机
    @Bean
    public FanoutExchange fanoutExchange() {
       return new FanoutExchange(EXCHANGE_NAME);
    }

    // 绑定队列与交换机到关系
    @Bean
    public Binding bindFanoutQueue1(Queue fanoutQueue1, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }

    @Bean
    public Binding bindFanoutQueue2(Queue fanoutQueue2, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }
}
