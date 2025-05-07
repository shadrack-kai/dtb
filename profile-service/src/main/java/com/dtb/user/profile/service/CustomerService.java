package com.dtb.user.profile.service;

import com.dtb.user.profile.model.dto.CustomerProfileDto;
import com.dtb.user.profile.model.request.CreateProfileRequest;

public interface CustomerService {
    CustomerProfileDto createCustomerProfile(CreateProfileRequest profileRequest);
    CustomerProfileDto updateCustomerProfile(CreateProfileRequest profileRequest, String customerId);
    CustomerProfileDto getCustomerProfile(String customerId);
}
