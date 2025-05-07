package com.dtb.user.profile;

import com.dtb.common.util.security.TokenProvider;
import com.dtb.user.authentication.model.dto.request.LoginRequest;
import com.dtb.user.profile.model.dto.CustomerProfileDto;
import com.dtb.user.profile.model.dto.PhoneNumberType;
import com.dtb.user.profile.model.request.CreateProfileRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
class ProfileServiceApplicationTests {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .findAndRegisterModules();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenProvider tokenProvider;

    private static String CUSTOMER_ID;

    @Order(1)
    @SneakyThrows
    @DisplayName("Login with Valid Credentials")
    @Test
    void authenticateUser_shouldReturnSuccess_whenValidCredentials() {
        //given
        var loginRequestDto = LoginRequest.builder()
                .email("test-admin@gmail.com")
                .password("TestAdmin!")
                .build();

        //then
        mockMvc.perform(post(("/users/api/v1/login"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Order(2)
    @SneakyThrows
    @DisplayName("Login with Invalid Credentials")
    @Test
    void authenticateUser_shouldReturnUnauthorized_whenInvalidCredentials() {
        //given
        var loginRequestDto = LoginRequest.builder()
                .email("test@test.com")
                .password("Test!123")
                .build();

        //then
        mockMvc.perform(post(("/api/v1/login"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Order(3)
    @SneakyThrows
    @DisplayName("Create customer profile")
    @Test
    void createProfile_shouldReturnSuccess_whenProfileCreated() {
        //given
        var profileRequest = CreateProfileRequest.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .dateOfBirth(LocalDate.now().minusYears(20))
                .gender("M")
                .idNumber("12345678")
                .email(CreateProfileRequest.Email.builder().address("test1@test.com").build())
                .phoneNumber(CreateProfileRequest.PhoneNumber.builder().number("0712345678").type(PhoneNumberType.MOBILE).build())
                .build();

        //then
        var mvcResult = mockMvc.perform(post(("/profile/api/v1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(createToken()))
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.firstName").value(profileRequest.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(profileRequest.getLastName()))
                .andExpect(jsonPath("$.idNumber").value(profileRequest.getIdNumber()))
                .andExpect(jsonPath("$.email.address").value(profileRequest.getEmail().getAddress()))
                .andExpect(jsonPath("$.phoneNumber.number").value(profileRequest.getPhoneNumber().getNumber()))
                .andExpect(jsonPath("$.phoneNumber.type").value(profileRequest.getPhoneNumber().getType().name()))
                .andReturn();

        var customerProfile = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CustomerProfileDto.class);
        CUSTOMER_ID = customerProfile.getIdNumber();
    }

    @Order(4)
    @SneakyThrows
    @DisplayName("Get customer profile")
    @Test
    void getProfile_shouldReturnSuccess_whenProfileFound() {
        //given
        var profile = CreateProfileRequest.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .dateOfBirth(LocalDate.now().minusYears(20))
                .gender("M")
                .idNumber("12345678")
                .email(CreateProfileRequest.Email.builder().address("test1@test.com").build())
                .phoneNumber(CreateProfileRequest.PhoneNumber.builder().number("0712345678").type(PhoneNumberType.MOBILE).build())
                .build();

        //then
        mockMvc.perform(get("/profile/api/v1/{id}", CUSTOMER_ID)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(createToken()))
                        .content(objectMapper.writeValueAsString(profile)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.firstName").value(profile.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(profile.getLastName()))
                .andExpect(jsonPath("$.idNumber").value(profile.getIdNumber()))
                .andExpect(jsonPath("$.email.address").value(profile.getEmail().getAddress()))
                .andExpect(jsonPath("$.phoneNumber.number").value(profile.getPhoneNumber().getNumber()))
                .andExpect(jsonPath("$.phoneNumber.type").value(profile.getPhoneNumber().getType().name()));
    }

    @Order(5)
    @SneakyThrows
    @DisplayName("Get customer profile with invalid ID")
    @Test
    void getProfile_shouldReturnNotFound_whenProfileNotFound() {
        //given
        var profile = CreateProfileRequest.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .dateOfBirth(LocalDate.now().minusYears(20))
                .gender("M")
                .idNumber("12345678")
                .email(CreateProfileRequest.Email.builder().address("test1@test.com").build())
                .phoneNumber(CreateProfileRequest.PhoneNumber.builder().number("0712345678").type(PhoneNumberType.MOBILE).build())
                .build();

        //then
        mockMvc.perform(get("/profile/api/v1/{id}", 0)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(createToken()))
                        .content(objectMapper.writeValueAsString(profile)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Order(5)
    @SneakyThrows
    @DisplayName("Update customer profile")
    @Test
    void updateProfile_shouldReturnSuccess_whenProfileFound() {
        //given
        var profile = CreateProfileRequest.builder()
                .firstName("First_Name")
                .lastName("Last_Name")
                .dateOfBirth(LocalDate.now().minusYears(20))
                .gender("M")
                .idNumber("68712345")
                .email(CreateProfileRequest.Email.builder().address("test2@test.com").build())
                .phoneNumber(CreateProfileRequest.PhoneNumber.builder().number("0712345678").type(PhoneNumberType.MOBILE).build())
                .build();

        //then
        mockMvc.perform(put("/profile/api/v1/{id}", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profile))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(createToken()))
                        .content(objectMapper.writeValueAsString(profile)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.firstName").value(profile.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(profile.getLastName()))
                .andExpect(jsonPath("$.idNumber").value(profile.getIdNumber()))
                .andExpect(jsonPath("$.email.address").value(profile.getEmail().getAddress()))
                .andExpect(jsonPath("$.phoneNumber.number").value(profile.getPhoneNumber().getNumber()))
                .andExpect(jsonPath("$.phoneNumber.type").value(profile.getPhoneNumber().getType().name()));
    }

    private String createToken() {
        return tokenProvider.createJwt("1", List.of("ADMIN"));
    }

}
