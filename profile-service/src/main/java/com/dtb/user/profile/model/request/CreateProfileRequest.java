package com.dtb.user.profile.model.request;

import com.dtb.user.profile.model.dto.PhoneNumberType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProfileRequest {

    @NotNull
    private String firstName;
    private String middleName;

    @NotNull
    private String lastName;

    @NotNull
    private String gender;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotNull
    private String idNumber;

    /**
     * phoneNumber: can be changed to a list to accept multiple phone number types
     */
    private PhoneNumber phoneNumber;
    private Email email;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PhoneNumber {
        private String number;
        private PhoneNumberType type;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Email {
        private String address;
    }

}