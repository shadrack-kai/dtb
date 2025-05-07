package com.dtb.account.service.impl;

import com.dtb.account.model.TransactionStatus;
import com.dtb.account.model.dto.AccountEventDto;
import com.dtb.account.repository.AccountRepository;
import com.dtb.account.repository.TransactionRepository;
import com.dtb.account.repository.jpa.TransactionEntity;
import com.dtb.account.service.MessagingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountBalanceServiceImpl {

    private final AccountRepository accountRepository;
    private final MessagingService messagingService;
    private final TransactionRepository transactionRepository;

    @Transactional
    public synchronized void updateAccountBalance(AccountEventDto accountEvent) {
        if(!transactionRepository.existsById(accountEvent.id())) {
            //can check by status and retry transaction

            log.info("Update account balance for accountId: {}", accountEvent.accountId());
            accountRepository.findById(accountEvent.accountId())
                    .ifPresentOrElse(accountEntity -> {
                        var balance = createBalance(accountEvent, accountEntity.getBalance());
                        accountEntity.setBalance(balance);
                        accountRepository.save(accountEntity);

                        transactionRepository.save(TransactionEntity.builder()
                                .id(accountEvent.id())
                                .accountId(accountEvent.accountId())
                                .status(TransactionStatus.COMPLETE)
                                .createdAt(LocalDateTime.now())
                                .build());

                        log.info("Account balance update for accountId: {} successful", accountEvent.accountId());

                        var updatedAccountEvent = AccountEventDto.builder()
                                .id(accountEvent.id())
                                .accountId(accountEvent.accountId())
                                .amount(accountEvent.amount())
                                .direction(accountEvent.direction())
                                .transactionType(accountEvent.transactionType())
                                .status(TransactionStatus.COMPLETE)
                                .build();
                        messagingService.sendAccountUpdateEvent(updatedAccountEvent);
                    }, () -> log.info("Account balance update failed. Account with accountId: {} does not exist", accountEvent.accountId()));
        }
    }

    private BigDecimal createBalance(AccountEventDto accountEvent, BigDecimal balance) {
        return AccountEventDto.Direction.IN.equals(accountEvent.direction()) ?
            balance.add(accountEvent.amount()) : balance.subtract(accountEvent.amount());
    }
}
