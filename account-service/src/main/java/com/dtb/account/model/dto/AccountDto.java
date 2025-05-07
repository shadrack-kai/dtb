package com.dtb.account.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {

    private Long id;
    // private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private Long customerId;
    private String accountHolderName;
    private AccountStatus status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;

    //other account details can be added

    public enum AccountType {
        CURRENT,
        SAVINGS
    }

    public enum AccountStatus {
        PENDING,
        ACTIVE,
        DEACTIVATED,
        //other status can be added
    }

}
