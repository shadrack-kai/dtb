package com.dtb.account;

import com.dtb.account.config.ConfigProperties;
import com.dtb.account.model.dto.AccountDto;
import com.dtb.account.model.dto.request.CreateAccountRequestDto;
import com.dtb.account.model.dto.request.UpdateAccountRequestDto;
import com.dtb.common.util.security.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
class AccountApplicationTests {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .findAndRegisterModules();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenProvider tokenProvider;

    private static Long accountId;

    @Order(1)
    @SneakyThrows
    @Test
    void createAccount_shouldReturnSuccess_whenAccountCreated() {
        //given
        var createAccountRequest = CreateAccountRequestDto.builder()
                .accountHolderName("Account Holder Name")
                .accountType(AccountDto.AccountType.CURRENT)
                .customerId(100L)
                .build();
        var request = objectMapper.writeValueAsString(createAccountRequest);

        //when
        stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlEqualTo("/profile/api/v1/%s".formatted(createAccountRequest.getCustomerId())))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"firstName\": \"Customer Name\"}")));

        //then
        MvcResult mvcResult = mockMvc.perform(post("/account/api/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(createToken()))
                        .content(request))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        AccountDto accountDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AccountDto.class);
        accountId = accountDto.getId();
    }

    @Order(1)
    @SneakyThrows
    @Test
    void createAccount_shouldReturnNotFound_whenCustomerProfileNotFound() {
        //given
        var createAccountRequest = CreateAccountRequestDto.builder()
                .accountHolderName("Account Holder Name")
                .accountType(AccountDto.AccountType.CURRENT)
                .customerId(101L)
                .build();
        var request = objectMapper.writeValueAsString(createAccountRequest);

        //when
        stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlEqualTo("/profile/api/v1/%s".formatted(createAccountRequest.getCustomerId())))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"Customer profile not found\"}")));

        //then
        mockMvc.perform(post("/account/api/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(createToken()))
                        .content(request))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Order(2)
    @SneakyThrows
    @Test
    void createAccount_shouldReturnForbidden_whenRoleNotAllowed() {

        var request = objectMapper.writeValueAsString(CreateAccountRequestDto.builder()
                .accountHolderName("Account Holder Name")
                .accountType(AccountDto.AccountType.CURRENT)
                .customerId(100L)
                .build());

        //then
        mockMvc.perform(post("/account/api/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(tokenProvider.createJwt("1", List.of("USER"))))
                        .content(request))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Order(3)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @SneakyThrows
    @Test
    void updateAccount_shouldReturnSuccess_whenAccountUpdated() {

        var request = objectMapper.writeValueAsString(UpdateAccountRequestDto.builder()
                .accountHolderName("Account Holder Name")
                .accountType(AccountDto.AccountType.CURRENT)
                .customerId(100L)
                .build());

        //then
        assertNotNull(accountId);
        mockMvc.perform(patch("/account/api/v1/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(createToken()))
                        .content(request))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Order(4)
    @SneakyThrows
    @Test
    void updateAccount_shouldReturnNotFound_whenAccountNotFound() {

        var request = objectMapper.writeValueAsString(UpdateAccountRequestDto.builder()
                .accountHolderName("Account Holder Name")
                .accountType(AccountDto.AccountType.CURRENT)
                .customerId(100L)
                .build());

        //then
        mockMvc.perform(patch("/account/api/v1/{id}",0)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(createToken()))
                        .content(request))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Order(5)
    @SneakyThrows
    @Test
    void activateAccount_shouldReturnSuccess_whenAccountActivated() {

        //then
        assertNotNull(accountId);
        mockMvc.perform(post("/account/api/v1/{id}/activate", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(createToken())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(AccountDto.AccountStatus.ACTIVE.name()));
    }

    @Order(6)
    @SneakyThrows
    @Test
    void activateAccount_shouldReturnNotFound_whenAccountNotFound() {

        //then
        assertNotNull(accountId);
        mockMvc.perform(post("/account/api/v1/{id}/activate", 0)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(createToken())))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Order(7)
    @SneakyThrows
    @Test
    void getBalance_shouldReturnSuccess_whenAccountBalanceFound() {

        //then
        assertNotNull(accountId);
        mockMvc.perform(get("/account/api/v1/{id}/balance", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(createToken())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableBalance").value(0))
                .andExpect(jsonPath("$.ledgerBalance").exists())
                .andExpect(jsonPath("$.balanceAsAt").exists());
    }

    @Order(8)
    @SneakyThrows
    @Test
    void deactivateAccount_shouldReturnSuccess_whenAccountDeactivated() {

        //then
        assertNotNull(accountId);
        mockMvc.perform(post("/account/api/v1/{id}/deactivate", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(createToken())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(AccountDto.AccountStatus.DEACTIVATED.name()));
    }

    @Order(9)
    @SneakyThrows
    @Test
    void deactivateAccount_shouldReturnNotFound_whenAccountNotFound() {

        //then
        assertNotNull(accountId);
        mockMvc.perform(post("/account/api/v1/{id}/deactivate", 0)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(createToken())))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private String createToken() {
        return tokenProvider.createJwt("1", List.of("ADMIN"));
    }

}
