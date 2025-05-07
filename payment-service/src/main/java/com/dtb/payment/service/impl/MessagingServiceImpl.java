package com.dtb.payment.service.impl;

import com.dtb.payment.config.ConfigProperties;
import com.dtb.payment.model.dto.AccountEventDto;
import com.dtb.payment.service.MessagingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessagingServiceImpl implements MessagingService {

    private final RabbitTemplate rabbitTemplate;
    private final Exchange exchange;
    private final ObjectMapper objectMapper;
    private final ConfigProperties properties;

    @Override
    public void sendAccountUpdateEvent(AccountEventDto accountEvent) {
        rabbitTemplate.convertAndSend(exchange.getName(), properties.getRmq().getAccount().getRoutingKey(), createMessage(accountEvent));
        log.info("Account balance update event sent: {}", accountEvent);
    }

    @Override
    public void sendNotificationEvent(AccountEventDto accountEvent) {
        rabbitTemplate.convertAndSend(exchange.getName(), properties.getRmq().getEvent().getRoutingKey(), createMessage(accountEvent));
        log.info("Notification for account balance update event sent: {}", accountEvent);
    }

    private String createMessage(Object accountEventDto) {
        try {
            return objectMapper.writeValueAsString(accountEventDto);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize update account message");
            return "{}";
        }
    }

}