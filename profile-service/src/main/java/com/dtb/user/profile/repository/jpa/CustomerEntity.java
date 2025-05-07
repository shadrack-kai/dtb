package com.dtb.user.profile.repository.jpa;

import com.dtb.user.profile.model.dto.PhoneNumberType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customers", uniqueConstraints = {
                @UniqueConstraint(name = "customers_unique_id_idx", columnNames = {"uniqueId"})
        })
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uniqueId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private LocalDate dateOfBirth;
    private String emailAddress;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private PhoneNumberType phoneNumberType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

}
