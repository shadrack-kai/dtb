package com.dtb.payment.service.impl;

import com.dtb.payment.exception.InsufficientBalanceException;
import com.dtb.payment.model.EntryType;
import com.dtb.payment.model.TransactionType;
import com.dtb.payment.model.dto.request.CreatePaymentRequest;
import com.dtb.payment.model.dto.request.CreateTransferRequest;
import com.dtb.payment.repository.TransactionRepository;
import com.dtb.payment.repository.jpa.TransactionEntity;
import com.dtb.payment.service.MessagingService;
import com.dtb.payment.service.PaymentService;
import com.dtb.payment.service.client.AccountService;
import com.dtb.payment.service.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper mapper;
    private final AccountService accountService;
    private final MessagingService messagingService;

    @Override
    public void processTopUp(CreatePaymentRequest request, String token) {
        accountService.getAccountBalance(request.getAccountId().toString(), token);
        var transactionEntity = mapper.toEntity(request, EntryType.CREDIT, TransactionType.TOP_UP);
        processPayment(transactionEntity);
    }

    @Override
    public void processWithdrawal(CreatePaymentRequest request, String token) {
        var transactionEntity = mapper.toEntity(request, EntryType.DEBIT, TransactionType.WITHDRAWAL);
        var accountBalance = accountService.getAccountBalance(request.getAccountId().toString(), token);
        if(accountBalance.availableBalance().compareTo(transactionEntity.getAmount()) < 0){
            throw new InsufficientBalanceException("Insufficient account balance for accountId: %s".formatted(request.getAccountId()));
        }
        processPayment(transactionEntity);
    }

    @Override
    public void processTransfer(CreateTransferRequest request, String token) {
        var accountBalance = accountService.getAccountBalance(request.getFromAccount(), token);
        if(accountBalance.availableBalance().compareTo(request.getAmount()) < 0){
            throw new InsufficientBalanceException("Insufficient account balance for accountId: %s".formatted(request.getFromAccount()));
        }
        var transactionEntity = mapper.toEntity(request, EntryType.DEBIT);
        var savedTransactionEntity = transactionRepository.save(transactionEntity);
        var accountEventDto = mapper.toEventDto(savedTransactionEntity);
        messagingService.sendAccountUpdateEvent(accountEventDto);
    }

    private void processPayment(TransactionEntity transactionEntity) {
        var savedTransactionEntity = transactionRepository.save(transactionEntity);
        var eventDto = mapper.toEventDto(savedTransactionEntity);
        messagingService.sendAccountUpdateEvent(eventDto);
    }
}