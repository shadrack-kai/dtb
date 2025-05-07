package com.dtb.account.repository;

import com.dtb.account.model.TransactionStatus;
import com.dtb.account.repository.jpa.TransactionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    @Query(value = "INSERT INTO transactions (id, account_id, status, created_at) " +
            "VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
    @Modifying
    @Transactional
    void save(Long id, Long accountId, TransactionStatus status, LocalDateTime createdAt);
}
