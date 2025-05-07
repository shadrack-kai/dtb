package com.dtb.payment.model.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record AccountBalanceDto (
    BigDecimal availableBalance,
    BigDecimal ledgerBalance,
    LocalDateTime balanceAsAt
){}