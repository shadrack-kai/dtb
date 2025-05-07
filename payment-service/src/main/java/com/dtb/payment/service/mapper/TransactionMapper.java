package com.dtb.payment.service.mapper;

import com.dtb.payment.model.EntryType;
import com.dtb.payment.model.TransactionStatus;
import com.dtb.payment.model.TransactionType;
import com.dtb.payment.model.dto.AccountEventDto;
import com.dtb.payment.model.dto.request.CreatePaymentRequest;
import com.dtb.payment.model.dto.request.CreateTransferRequest;
import com.dtb.payment.repository.jpa.TransactionEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransactionMapper {

    public TransactionEntity toEntity(CreatePaymentRequest paymentRequest, EntryType entryType, TransactionType transactionType) {
        return TransactionEntity.builder()
                .accountId(paymentRequest.getAccountId().toString())
                //.beneficiaryAccount(paymentRequest.getAccountId().toString())
                .amount(paymentRequest.getAmount())
                .paymentMethod(paymentRequest.getMethod())
                .entryType(entryType)
                .transactionType(transactionType)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public TransactionEntity toEntity(CreateTransferRequest transferRequest,
                                      EntryType entryType) {
        var builder = TransactionEntity.builder()
                .accountId(transferRequest.getFromAccount())
                .beneficiaryAccount(transferRequest.getToAccount())
                .amount(transferRequest.getAmount())
                .createdAt(LocalDateTime.now())
                .entryType(entryType)
                .transactionStatus(TransactionStatus.PENDING)
                .transactionType(TransactionType.TRANSFER);
        return builder.build();
    }

    public TransactionEntity toEntity(TransactionEntity transactionEntity) {
        transactionEntity.setId(null);
        transactionEntity.setAccountId(transactionEntity.getBeneficiaryAccount());
        transactionEntity.setBeneficiaryAccount(null);
        transactionEntity.setEntryType(EntryType.CREDIT);
        transactionEntity.setTransactionStatus(TransactionStatus.PENDING);
        return transactionEntity;
    }

    public AccountEventDto toEventDto(TransactionEntity transactionEntity) {
        var direction = EntryType.CREDIT.equals(transactionEntity.getEntryType()) ? AccountEventDto.Direction.IN : AccountEventDto.Direction.OUT;
        return AccountEventDto.builder()
                .id(transactionEntity.getId())
                .accountId(transactionEntity.getAccountId())
                .amount(transactionEntity.getAmount())
                .direction(direction)
                .transactionType(transactionEntity.getTransactionType())
                .status(TransactionStatus.PENDING)
                .transactionType(transactionEntity.getTransactionType())
                .build();
    }
}