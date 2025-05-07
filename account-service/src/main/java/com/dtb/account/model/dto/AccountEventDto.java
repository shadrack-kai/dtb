package com.dtb.account.model.dto;

import com.dtb.account.model.TransactionStatus;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountEventDto(
        Long id,
        Long accountId,
        Direction direction,
        BigDecimal amount,
        TransactionType transactionType,
        TransactionStatus status
) {
    public enum Direction {
        IN,
        OUT
    }

    public enum TransactionType {
        TOP_UP,
        WITHDRAWAL,
        TRANSFER
    }
}