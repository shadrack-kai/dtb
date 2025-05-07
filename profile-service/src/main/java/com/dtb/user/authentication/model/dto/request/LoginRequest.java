package com.dtb.user.authentication.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @Schema(example = "test-admin@gmail.com")
    @NotNull
    private String email;

    @Schema(example = "TestAdmin!")
    @NotNull
    private String password;
}
