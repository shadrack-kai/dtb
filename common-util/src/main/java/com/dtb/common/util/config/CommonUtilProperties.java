package com.dtb.common.util.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("common.util")
public class CommonUtilProperties {

    private JwtTokenProperties jwtToken;
    private RestClientProperties restClient = new RestClientProperties();
    private String requestMatchers;

    @Getter
    @Setter
    public static class JwtTokenProperties {
        private String secretKey;
        private Integer validity;
    }

    @Getter
    @Setter
    public static class RestClientProperties {
        private Integer connectTimeout = 300;
        private Integer readTimeout = 600;
    }

}