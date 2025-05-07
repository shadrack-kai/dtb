package com.dtb.common.util.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@RequiredArgsConstructor
@Configuration
public class RestTemplateConfig {

    private final CommonUtilProperties properties;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .connectTimeout(Duration.ofSeconds(properties.getRestClient().getConnectTimeout()))
                .readTimeout(Duration.ofSeconds(properties.getRestClient().getReadTimeout()))
                .build();
    }
}