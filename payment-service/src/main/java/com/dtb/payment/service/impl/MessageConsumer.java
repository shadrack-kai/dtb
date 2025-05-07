package com.dtb.payment.service.impl;

import com.dtb.payment.model.TransactionStatus;
import com.dtb.payment.model.dto.AccountEventDto;
import com.dtb.payment.repository.TransactionRepository;
import com.dtb.payment.service.MessagingService;
import com.dtb.payment.service.mapper.TransactionMapper;
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
    private final TransactionRepository transactionRepository;
    private final MessagingService messagingService;
    private final TransactionMapper transactionMapper;

    @RabbitListener(queues = "${service.rmq.payment.queue-name}")
    public void receive(String message) {
        log.info("Account balance update event received: {}", message);
        var accountEvent = createAccountDto(message);
        transactionRepository.findById(accountEvent.id())
                        .ifPresent(transactionEntity -> {
                            transactionEntity.setTransactionStatus(TransactionStatus.COMPLETE);
                            transactionRepository.save(transactionEntity);
                            if(transactionEntity.getBeneficiaryAccount() != null) {
                                var creditTransactionEntity = transactionMapper.toEntity(transactionEntity);
                                var savedTransactionEntity = transactionRepository.save(creditTransactionEntity);
                                var accountEventDto = transactionMapper.toEventDto(savedTransactionEntity);
                                messagingService.sendAccountUpdateEvent(accountEventDto);
                            }
                        });
        messagingService.sendNotificationEvent(accountEvent);
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
