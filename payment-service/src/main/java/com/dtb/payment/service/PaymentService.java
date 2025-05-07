package com.dtb.payment.service;

import com.dtb.payment.model.dto.request.CreatePaymentRequest;
import com.dtb.payment.model.dto.request.CreateTransferRequest;

public interface PaymentService {
    void processTopUp(CreatePaymentRequest request, String token);

    void processWithdrawal(CreatePaymentRequest request, String token);

    void processTransfer(CreateTransferRequest request, String token);
}
