package com.dtb.account.service.client.impl;

import com.dtb.account.config.ConfigProperties;
import com.dtb.account.model.client.profile.CustomerProfileDto;
import com.dtb.account.service.client.CustomerProfile;
import com.dtb.common.util.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerProfileImpl implements CustomerProfile {

    private final RestTemplate restTemplate;
    private final ConfigProperties properties;

    @Override
    public CustomerProfileDto getCustomer(Long customerId, String token) {
        var responseEntity = restTemplate.exchange(properties.getClient().getProfile().getUrl(),
                HttpMethod.GET,
                createHttpEntity(token),
                CustomerProfileDto.class,
                customerId);
        log.info("Customer profile with Id: {} found", customerId);
        return responseEntity.getBody();
    }

    private HttpEntity<HttpHeaders> createHttpEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, token);
        return new HttpEntity<>(headers);
    }
}