package com.dtb.user.authentication.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCredentialsRequest {
    private String email;
    private String password;
    private String confirmPassword;
}
