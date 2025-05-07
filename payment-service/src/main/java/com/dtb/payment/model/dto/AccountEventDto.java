package com.dtb.payment.model.dto;

import com.dtb.payment.model.TransactionStatus;
import com.dtb.payment.model.TransactionType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountEventDto(
        Long id,
        String accountId,
        Direction direction,
        BigDecimal amount,
        TransactionType transactionType,
        TransactionStatus status
) {
    public enum Direction {
        IN,
        OUT
    }
}