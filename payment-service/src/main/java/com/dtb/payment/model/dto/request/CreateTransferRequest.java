package com.dtb.payment.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTransferRequest {

    @NotNull
    private String fromAccount;

    @NotNull
    private String toAccount;

    @NotNull
    private BigDecimal amount;

    private Boolean isInternalTransfer;

}
