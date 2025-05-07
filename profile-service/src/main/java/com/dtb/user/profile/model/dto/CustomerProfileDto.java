package com.dtb.user.profile.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerProfileDto {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private LocalDate dateOfBirth;

    /**
     * idNumber: can be split to ID and ID type to accommodate other types like passport
     */
    private String idNumber;

    private PhoneNumber phoneNumber;
    private Email email;
    private AddressDetails addressDetails;

    //Alternative contact / Next of Kin details can also be added

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;

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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddressDetails {
        private String estate;
        private String street;
        private String building;
        //more address details can be added
    }
}