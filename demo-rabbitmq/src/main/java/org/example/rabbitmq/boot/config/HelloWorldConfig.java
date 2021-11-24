package org.example.rabbitmq.boot.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HelloWorld 模式，不需要 routingKey
 * 不需要定义交换机，只需要定义一个队列，所有消息都是通过队列进行转发
 */
@Configuration
public class HelloWorldConfig {
    public static final String QUEUE_NAME = "hello.world.queue";
    @Bean
    public Queue helloWorldQueue() {
        return new Queue(QUEUE_NAME);
    }
}
