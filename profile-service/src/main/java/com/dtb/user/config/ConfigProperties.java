package com.dtb.user.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("service")
public class ConfigProperties {

    private JwtConfig jwt;

    @Getter
    @Setter
    public static class JwtConfig {
        private Token token;
    }

    @Getter
    @Setter
    public static class Token {
        private String secretKey;
        private Integer validity;
    }

}