package com.dtb.account.service.impl;

import com.dtb.account.model.dto.AccountEventDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageConsumer {

    private final ObjectMapper objectMapper;
    private final AccountBalanceServiceImpl accountBalanceService;

    @RabbitListener(queues = "${service.rmq.account.queue-name}")
    public void receive(String message) {
        log.info("Account balance update event received: {}", message);
        var accountEvent = createAccountDto(message);
        accountBalanceService.updateAccountBalance(accountEvent);
    }

    private AccountEventDto createAccountDto(String message) {
        try {
            return objectMapper.readValue(message, AccountEventDto.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize message: {} to AccountEventDto.class", message);
            return AccountEventDto.builder().build();
        }
    }
}
