package com.dtb.account.repository;

import com.dtb.account.model.dto.AccountDto;
import com.dtb.account.model.dto.request.CreateAccountRequestDto;
import com.dtb.account.model.dto.request.UpdateAccountRequestDto;
import com.dtb.account.repository.jpa.AccountEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class AccountMapper {

    public AccountDto toDto(AccountEntity account) {
        return AccountDto.builder()
                .id(account.getId())
                .customerId(account.getCustomerId())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .accountHolderName(account.getAccountHolderName())
                .status(account.getAccountStatus())
                .createdAt(account.getCreatedAt())
                .createdBy(account.getCreatedBy())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

    public AccountEntity toEntity(CreateAccountRequestDto account) {
        var accountEntityBuilder = AccountEntity.builder()
                .customerId(account.getCustomerId())
                .accountType(account.getAccountType())
                .balance(BigDecimal.ZERO)
                .accountHolderName(account.getAccountHolderName())
                .accountStatus(AccountDto.AccountStatus.PENDING)
                .createdAt(LocalDateTime.now());

        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return accountEntityBuilder
                .createdBy(user.getUsername())
                .build();
    }

    public AccountEntity toEntity(AccountEntity existingAccount, UpdateAccountRequestDto account, Long accountId) {
        if(StringUtils.hasText(account.getAccountHolderName()))
            existingAccount.setAccountHolderName(account.getAccountHolderName());
        if(Objects.nonNull(account.getCustomerId()))
            existingAccount.setCustomerId(account.getCustomerId());
        if(Objects.nonNull(account.getAccountType()))
            existingAccount.setAccountType(account.getAccountType());
        if(Objects.nonNull(account.getAccountType()))
            existingAccount.setAccountStatus(account.getStatus());

        existingAccount.setUpdatedAt(LocalDateTime.now());
        existingAccount.setId(accountId);

        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        existingAccount.setUpdatedBy(user.getUsername());
        return existingAccount;

    }
}
