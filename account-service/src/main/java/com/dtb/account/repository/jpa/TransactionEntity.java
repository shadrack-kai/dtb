package com.dtb.account.repository.jpa;

import com.dtb.account.model.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    private Long id;

    private Long accountId;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private LocalDateTime createdAt;

}