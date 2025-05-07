package com.dtb.payment.model.dto.request;

import com.dtb.payment.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentRequest {

    private Long accountId;
    private BigDecimal amount;
    private PaymentMethod method;

}
