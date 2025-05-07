package com.dtb.account.repository;

import com.dtb.account.repository.jpa.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
}