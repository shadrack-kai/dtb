package com.dtb.payment.service.client.impl;

import com.dtb.payment.config.ConfigProperties;
import com.dtb.payment.model.dto.AccountBalanceDto;
import com.dtb.payment.service.client.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final RestTemplate restTemplate;
    private final ConfigProperties properties;

    @Override
    public AccountBalanceDto getAccountBalance(String accountId, String token) {
        var responseEntity = restTemplate.exchange(properties.getClient().getAccount().getUrl(),
                HttpMethod.GET,
                createHttpEntity(token),
                AccountBalanceDto.class,
                accountId);
        log.info("Account with Id: {} found", accountId);
        return responseEntity.getBody();
    }

    private HttpEntity<HttpHeaders> createHttpEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, token);
        return new HttpEntity<>(headers);
    }

}