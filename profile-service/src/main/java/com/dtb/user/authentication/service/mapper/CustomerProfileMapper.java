package com.dtb.user.authentication.service.mapper;

import com.dtb.user.profile.model.dto.CustomerProfileDto;
import com.dtb.user.profile.model.request.CreateProfileRequest;
import com.dtb.user.profile.repository.jpa.CustomerEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CustomerProfileMapper {

    public CustomerEntity toJpa(CreateProfileRequest profileRequest) {
        return CustomerEntity.builder()
                .firstName(profileRequest.getFirstName())
                .middleName(profileRequest.getMiddleName())
                .lastName(profileRequest.getLastName())
                .dateOfBirth(profileRequest.getDateOfBirth())
                .gender(profileRequest.getGender())
                .uniqueId(profileRequest.getIdNumber())
                .emailAddress(profileRequest.getEmail().getAddress())
                .phoneNumber(profileRequest.getPhoneNumber().getNumber())
                .phoneNumberType(profileRequest.getPhoneNumber().getType())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public CustomerProfileDto toDomain(CustomerEntity customerEntity) {
        return CustomerProfileDto.builder()
                .id(customerEntity.getId())
                .firstName(customerEntity.getFirstName())
                .middleName(customerEntity.getMiddleName())
                .lastName(customerEntity.getLastName())
                .dateOfBirth(customerEntity.getDateOfBirth())
                .gender(customerEntity.getGender())
                .idNumber(customerEntity.getUniqueId())
                .email(CustomerProfileDto.Email.builder()
                        .address(customerEntity.getEmailAddress())
                        .build())
                .phoneNumber(CustomerProfileDto.PhoneNumber.builder()
                        .number(customerEntity.getPhoneNumber())
                        .type(customerEntity.getPhoneNumberType())
                        .build())
                .createdAt(customerEntity.getCreatedAt())
                .createdBy(customerEntity.getCreatedBy())
                .updatedAt(customerEntity.getUpdatedAt())
                .build();
    }

}
