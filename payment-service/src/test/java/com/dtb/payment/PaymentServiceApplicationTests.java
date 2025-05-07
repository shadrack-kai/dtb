package com.dtb.payment;

import com.dtb.common.util.security.TokenProvider;
import com.dtb.payment.model.PaymentMethod;
import com.dtb.payment.model.dto.request.CreatePaymentRequest;
import com.dtb.payment.model.dto.request.CreateTransferRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
class PaymentServiceApplicationTests {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .findAndRegisterModules();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenProvider tokenProvider;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @Order(1)
    @SneakyThrows
    @Test
    void topUp_shouldReturnSuccess_whenAccountToppedUp() {
        //given
        var createPaymentRequest = CreatePaymentRequest.builder()
                .accountId(1L)
                .amount(BigDecimal.valueOf(100))
                .method(PaymentMethod.CASH)
                .build();
        var request = objectMapper.writeValueAsString(createPaymentRequest);

        //when
        doNothing().when(rabbitTemplate).convertAndSend(any());
        stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlEqualTo("/account/api/v1/%s/balance".formatted(createPaymentRequest.getAccountId())))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"availableBalance\": 0.00}")));

        //then
        mockMvc.perform(post("/payment/api/v1/topup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(createToken()))
                        .content(request))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Order(2)
    @SneakyThrows
    @Test
    void withdrawal_shouldReturnSuccess_whenAccountHasSufficientBalance() {
        //given
        var createPaymentRequest = CreatePaymentRequest.builder()
                .accountId(1L)
                .amount(BigDecimal.valueOf(80))
                .method(PaymentMethod.CASH)
                .build();
        var request = objectMapper.writeValueAsString(createPaymentRequest);

        //when
        doNothing().when(rabbitTemplate).convertAndSend(any());
        stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlEqualTo("/account/api/v1/%s/balance".formatted(createPaymentRequest.getAccountId())))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"availableBalance\": 100.00}")));

        //then
        mockMvc.perform(post("/payment/api/v1/withdrawal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(createToken()))
                        .content(request))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Order(3)
    @SneakyThrows
    @Test
    void withdrawal_shouldThrowError_whenAccountHasInsufficientBalance() {
        //given
        var createPaymentRequest = CreatePaymentRequest.builder()
                .accountId(1L)
                .amount(BigDecimal.valueOf(120))
                .method(PaymentMethod.CASH)
                .build();
        var request = objectMapper.writeValueAsString(createPaymentRequest);

        //when
        doNothing().when(rabbitTemplate).convertAndSend(any());
        stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlEqualTo("/account/api/v1/%s/balance".formatted(createPaymentRequest.getAccountId())))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"availableBalance\": 100.00}")));

        //then
        mockMvc.perform(post("/payment/api/v1/withdrawal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(createToken()))
                        .content(request))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Order(4)
    @SneakyThrows
    @Test
    void processTransfer_shouldReturnSuccess_whenAccountHasSufficientBalance() {
        //given
        var createPaymentRequest = CreateTransferRequest.builder()
                .fromAccount("1")
                .toAccount("2")
                .amount(BigDecimal.valueOf(80))
                .isInternalTransfer(Boolean.TRUE)
                .build();
        var request = objectMapper.writeValueAsString(createPaymentRequest);

        //when
        doNothing().when(rabbitTemplate).convertAndSend(any());
        stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlEqualTo("/account/api/v1/%s/balance".formatted(createPaymentRequest.getFromAccount())))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"availableBalance\": 100.00}")));

        //then
        mockMvc.perform(post("/payment/api/v1/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(createToken()))
                        .content(request))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Order(5)
    @SneakyThrows
    @Test
    void processTransfer_shouldThrowError_whenAccountHasInsufficientBalance() {
        //given
        var createPaymentRequest = CreateTransferRequest.builder()
                .fromAccount("1")
                .toAccount("2")
                .amount(BigDecimal.valueOf(105))
                .isInternalTransfer(Boolean.TRUE)
                .build();
        var request = objectMapper.writeValueAsString(createPaymentRequest);

        //when
        doNothing().when(rabbitTemplate).convertAndSend(any());
        stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlEqualTo("/account/api/v1/%s/balance".formatted(createPaymentRequest.getFromAccount())))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"availableBalance\": 100.00}")));

        //then
        mockMvc.perform(post("/payment/api/v1/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(createToken()))
                        .content(request))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private String createToken() {
        return tokenProvider.createJwt("1", List.of("ADMIN"));
    }
}