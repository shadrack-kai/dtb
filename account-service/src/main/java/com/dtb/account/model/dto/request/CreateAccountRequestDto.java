package com.dtb.account.model.dto.request;

import com.dtb.account.model.dto.AccountDto;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAccountRequestDto {

    @NotNull
    private AccountDto.AccountType accountType;

    @NotNull
    private Long customerId;

    @NotNull
    private String accountHolderName;

}