package org.example.rabbitmq.boot.config;

import org.example.rabbitmq.boot.confirm.MessageConfirm;
import org.example.rabbitmq.boot.confirm.MessageReturn;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SerializerMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "boot_exchange_topic";
    public static final String QUEUE_NAME = "boot_queue";
    public static final String ROUTING_KEY = "boot.#";

    // 1、声明交换机
    @Bean("bootExchange")
    public Exchange bootExchange() {
        // durable 是否持久化
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    // 2、声明队列
    @Bean("bootQueue")
    public Queue bootQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    // 3、绑定交换机与队列的关系
    @Bean
    public Binding bindingBoot(@Qualifier("bootQueue") Queue bootQueue, @Qualifier("bootExchange") Exchange bootExchange) {
        return BindingBuilder.bind(bootQueue).to(bootExchange).with(ROUTING_KEY).noargs();
    }

    @Bean
    // @Scope("prototype")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMandatory(true);
        template.setMessageConverter(new SerializerMessageConverter());
        // 设置 Confirm Callback
        template.setConfirmCallback(new MessageConfirm());
        // 设置 Return Callback
        template.setReturnCallback(new MessageReturn());
        // 消息手动签收
        template.containerAckMode(AcknowledgeMode.MANUAL);
        return template;
    }

    // ============== ttl + dlx 配置 ==============

    public static final String EXCHANGE_DLX = "exchange_dlx";
    public static final String QUEUE_DLX = "queue_dlx";
    public static final String ROUTING_DLX_KEY = "dlx.#";

    @Bean("dlxExchange")
    public Exchange dlxExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_DLX).durable(true).build();
    }

    @Bean("dlxQueue")
    public Queue dlxQueue() {
        return QueueBuilder.durable(QUEUE_DLX).build();
    }

    @Bean
    public Binding bindingDlx(Queue dlxQueue, Exchange dlxExchange) {
        return BindingBuilder.bind(dlxQueue).to(dlxExchange).with(ROUTING_DLX_KEY).noargs();
    }

    public static final String EXCHANGE_TTL = "exchange_ttl";
    public static final String QUEUE_TTL = "queue_ttl";
    public static final String ROUTING_TTL_KEY = "ttl.#";


    @Bean("ttlExchange")
    public Exchange ttlExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_TTL).durable(true).build();
    }

    @Bean("ttlQueue")
    public Queue ttlQueue() {
        Map<String, Object> args = new HashMap<>();
        // 绑定死信交换机
        args.put("x-dead-letter-exchange", EXCHANGE_DLX);
        // 与死信交换机通讯的 routingKey
        args.put("x-dead-letter-routing-key", "dlx.ttl.cancel");
        // 消息存活时间
        args.put("x-message-ttl", 10000);
        // 最大长度
        args.put("x-max-length", 10);
        return QueueBuilder.durable(QUEUE_TTL).withArguments(args).build();
    }

    @Bean
    public Binding bindingTtl(Queue ttlQueue, Exchange ttlExchange) {
        return BindingBuilder.bind(ttlQueue).to(ttlExchange).with(ROUTING_TTL_KEY).noargs();
    }

}
