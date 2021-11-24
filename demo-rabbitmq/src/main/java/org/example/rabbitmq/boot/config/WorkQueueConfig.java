package org.example.rabbitmq.boot.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * work 模式，不需要 routingKey
 * 不需要定义交换机，只需要定义一个队列，所有消息都是通过队列进行转发
 * 每个队列都从交换机中获取分配到到数据（默认是轮询分配）
 */
@Configuration
public class WorkQueueConfig {

    public static final String QUEUE_NAME = "work.queue";

    @Bean
    public Queue workQueue() {
        return new Queue(QUEUE_NAME, true);
    }

}
