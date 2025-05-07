package com.dtb.user.profile.service.impl;

import com.dtb.common.util.exception.ResourceNotFoundException;
import com.dtb.user.authentication.service.mapper.CustomerProfileMapper;
import com.dtb.user.profile.model.dto.CustomerProfileDto;
import com.dtb.user.profile.model.request.CreateProfileRequest;
import com.dtb.user.profile.repository.CustomerProfileRepository;
import com.dtb.user.profile.repository.jpa.CustomerEntity;
import com.dtb.user.profile.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerProfileMapper mapper;
    private final CustomerProfileRepository customerProfileRepository;

    @Override
    public CustomerProfileDto createCustomerProfile(CreateProfileRequest profileRequest) {
        var customerEntity = mapper.toJpa(profileRequest);
        var savedCustomerEntity = customerProfileRepository.save(customerEntity);
        return mapper.toDomain(savedCustomerEntity);
    }

    @Override
    public CustomerProfileDto updateCustomerProfile(CreateProfileRequest profileRequest, String customerId) {
        return customerProfileRepository.findByUniqueId(customerId)
                .map(customerEntity -> {
                    var customerProfile = mapper.toJpa(profileRequest);
                    customerProfile.setId(customerEntity.getId());
                    customerProfile.setUpdatedAt(LocalDateTime.now());
                    CustomerEntity savedCustomerEntity = customerProfileRepository.save(customerProfile);
                    return mapper.toDomain(savedCustomerEntity);
                })
                .orElseThrow(() -> {
                    return new ResourceNotFoundException("Customer profile with ID: %s not found".formatted(customerId));
                });
    }

    @Override
    public CustomerProfileDto getCustomerProfile(String customerId) {
        return customerProfileRepository.findByUniqueId(customerId)
                .map(mapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile with ID: %s not found".formatted(customerId)));
    }

}
