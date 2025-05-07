package com.dtb.account.service.impl;

import com.dtb.account.model.dto.AccountBalanceDto;
import com.dtb.account.model.dto.AccountDto;
import com.dtb.account.model.dto.request.CreateAccountRequestDto;
import com.dtb.account.model.dto.request.UpdateAccountRequestDto;
import com.dtb.account.repository.AccountMapper;
import com.dtb.account.repository.AccountRepository;
import com.dtb.account.service.AccountService;
import com.dtb.account.service.client.CustomerProfile;
import com.dtb.common.util.exception.BadRequestException;
import com.dtb.common.util.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private static final String ACCOUNT_NOT_FOUND = "Account with accountId: %s not found";

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CustomerProfile customerProfile;

    @Override
    public AccountDto createAccount(CreateAccountRequestDto accountRequest, String token) {
        log.info("Create account for customerId: {}", accountRequest.getCustomerId());

        //Check customer profile exist by id
        customerProfile.getCustomer(accountRequest.getCustomerId(), token);

        var accountEntity = accountMapper.toEntity(accountRequest);
        var createdAccount = accountRepository.save(accountEntity);
        return accountMapper.toDto(createdAccount);
    }

    @Override
    public AccountDto updateAccount(UpdateAccountRequestDto accountRequest, Long accountId, String token) {
        log.info("Update account for customerId: {} and accountId: {}", accountRequest.getCustomerId(), accountId);
        return accountRepository.findById(accountId)
                .map(existAccountEntity -> {
                    var accountEntity = accountMapper.toEntity(existAccountEntity, accountRequest, accountId);
                    var savedAccountEntity = accountRepository.save(accountEntity);
                    return accountMapper.toDto(savedAccountEntity);
                })
                .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND.formatted(accountId)));
    }

    @Override
    public AccountBalanceDto getBalance(Long accountId) {
        log.info("Get account balance for accountId: {}", accountId);
        var accountEntity = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND.formatted(accountId)));

        if (!AccountDto.AccountStatus.ACTIVE.equals(accountEntity.getAccountStatus())) {
            throw new BadRequestException("Account with Id: %s is not ACTIVE".formatted(accountId));
        }
        return AccountBalanceDto.builder()
                .availableBalance(accountEntity.getBalance())
                .ledgerBalance(accountEntity.getBalance())
                .balanceAsAt(LocalDateTime.now())
                .build();
    }

    @Override
    public AccountDto activateAccount(Long accountId) {
        log.info("Activate account for accountId: {}", accountId);
        var account = accountRepository.findById(accountId);
        var accountEntity = account
                .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND.formatted(accountId)));
        if (AccountDto.AccountStatus.DEACTIVATED.equals(accountEntity.getAccountStatus())) {
            throw new BadRequestException("Account with ID: %s cannot be activated".formatted(accountId));
        }

        accountEntity.setAccountStatus(AccountDto.AccountStatus.ACTIVE);
        accountEntity.setUpdatedAt(LocalDateTime.now());
        var updatedAccountEntity = accountRepository.save(accountEntity);
        return accountMapper.toDto(updatedAccountEntity);
    }

    @Override
    public AccountDto deactivateAccount(Long accountId) {
        log.info("Deactivate account for accountId: {}", accountId);
        var accountEntity = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND.formatted(accountId)));
        if (accountEntity.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new BadRequestException("Account with ID: %s cannot be deactivated".formatted(accountId));
        }

        accountEntity.setAccountStatus(AccountDto.AccountStatus.DEACTIVATED);
        accountEntity.setUpdatedAt(LocalDateTime.now());
        var updatedAccountEntity = accountRepository.save(accountEntity);
        return accountMapper.toDto(updatedAccountEntity);
    }

}
