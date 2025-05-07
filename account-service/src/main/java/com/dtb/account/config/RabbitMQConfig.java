package com.dtb.account.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class RabbitMQConfig {

    private final ConfigProperties properties;

    @Bean
    public Queue queue(){
        return new Queue(properties.getRmq().getAccount().getQueueName());
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(properties.getRmq().getExchangeName());
    }

    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(properties.getRmq().getAccount().getRoutingKey());
    }

}
