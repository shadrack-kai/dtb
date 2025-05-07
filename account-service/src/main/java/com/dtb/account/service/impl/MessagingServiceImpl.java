package com.dtb.account.service.impl;

import com.dtb.account.config.ConfigProperties;
import com.dtb.account.model.dto.AccountEventDto;
import com.dtb.account.service.MessagingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

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
        String exchangeName = exchange.getName();
        rabbitTemplate.convertAndSend(exchangeName, properties.getRmq().getPayment().getRoutingKey(), createMessage(accountEvent));
        log.info("Account balance update event sent: {}", accountEvent);
    }

    private String createMessage(AccountEventDto accountEventDto) {
        try {
            return objectMapper.writeValueAsString(accountEventDto);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize update account message for transactionId: {}", accountEventDto.id());
            return "{}";
        }
    }

}