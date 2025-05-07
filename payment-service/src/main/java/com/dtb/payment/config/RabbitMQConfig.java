package com.dtb.payment.config;

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
    public Queue paymentQueue() {
        return new Queue(properties.getRmq().getPayment().getQueueName());
    }

    @Bean
    public Queue accountQueue() {
        return new Queue(properties.getRmq().getAccount().getQueueName());
    }

    @Bean
    public Queue eventsQueue() {
        return new Queue(properties.getRmq().getEvent().getQueueName());
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(properties.getRmq().getExchangeName());
    }

    @Bean
    public Binding paymentBinding() {
        return BindingBuilder
                .bind(paymentQueue())
                .to(exchange())
                .with(properties.getRmq().getPayment().getRoutingKey());
    }

    @Bean
    public Binding accountBinding() {
        return BindingBuilder
                .bind(accountQueue())
                .to(exchange())
                .with(properties.getRmq().getAccount().getRoutingKey());
    }

    @Bean
    public Binding eventsBinding() {
        return BindingBuilder
                .bind(eventsQueue())
                .to(exchange())
                .with(properties.getRmq().getEvent().getRoutingKey());
    }

}