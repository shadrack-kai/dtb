package com.dtb.events.config;

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
    public Queue eventsQueue() {
        return new Queue(properties.getRmq().getEvent().getQueueName());
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(properties.getRmq().getExchangeName());
    }

    @Bean
    public Binding eventsBinding() {
        return BindingBuilder
                .bind(eventsQueue())
                .to(exchange())
                .with(properties.getRmq().getExchangeName());
    }

}