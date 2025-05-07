package com.dtb.account.repository.jpa;

import com.dtb.account.model.dto.AccountDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "account")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(nullable = false, unique = true)
    //private String accountNumber;

    private BigDecimal balance;
    private Long customerId;
    private String accountHolderName;

    @Enumerated(EnumType.STRING)
    private AccountDto.AccountType accountType;

    @Enumerated(EnumType.STRING)
    private AccountDto.AccountStatus accountStatus;

    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
