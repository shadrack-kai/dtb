package com.dtb.user.profile.repository;

import com.dtb.user.profile.repository.jpa.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerProfileRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByUniqueId(String uniqueId);
    Optional<CustomerEntity> findByEmailAddress(String idNumber);
}