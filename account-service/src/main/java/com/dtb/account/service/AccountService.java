package com.dtb.account.service;

import com.dtb.account.model.dto.AccountBalanceDto;
import com.dtb.account.model.dto.AccountDto;
import com.dtb.account.model.dto.request.CreateAccountRequestDto;
import com.dtb.account.model.dto.request.UpdateAccountRequestDto;

public interface AccountService {

    AccountDto createAccount(CreateAccountRequestDto accountRequest, String token);

    AccountDto updateAccount(UpdateAccountRequestDto accountRequest, Long accountId, String token);

    AccountBalanceDto getBalance(Long accountId);

    AccountDto activateAccount(Long accountId);

    AccountDto deactivateAccount(Long accountId);
}