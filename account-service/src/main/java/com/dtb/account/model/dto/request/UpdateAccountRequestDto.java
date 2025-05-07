package com.dtb.account.model.dto.request;

import com.dtb.account.model.dto.AccountDto;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAccountRequestDto {

    private Long customerId;

    private String accountHolderName;

    private AccountDto.AccountStatus status;

    private AccountDto.AccountType accountType;
}