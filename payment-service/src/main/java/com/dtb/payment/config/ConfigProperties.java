package com.dtb.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "service")
public class ConfigProperties {

    private RestClient client;
    private RmqProperties rmq;

    @Data
    public static class RestClient {
        private AccountProperties account;
    }

    @Data
    public static class AccountProperties {
        private String url;
    }

    @Data
    public static class RmqProperties {
        private String exchangeName;
        private QueueProperties account;
        private QueueProperties payment;
        private QueueProperties event;
    }

    @Data
    public static class QueueProperties {
        private String queueName;
        private String routingKey;
    }
}