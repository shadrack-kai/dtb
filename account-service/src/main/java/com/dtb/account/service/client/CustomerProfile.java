package com.dtb.account.service.client;

import com.dtb.account.model.client.profile.CustomerProfileDto;

public interface CustomerProfile {
    CustomerProfileDto getCustomer(Long customerId, String token);
}
