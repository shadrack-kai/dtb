package com.dtb.payment.repository;

import com.dtb.payment.repository.jpa.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}