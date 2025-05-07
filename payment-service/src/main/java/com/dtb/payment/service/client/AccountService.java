package com.dtb.payment.service.client;

import com.dtb.payment.model.dto.AccountBalanceDto;

public interface AccountService {
    AccountBalanceDto getAccountBalance(String accountId, String token);
}