package com.dtb.user.authentication.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private String token;
    private TokenType type;
    private Integer expiresIn;

    @Getter
    public enum TokenType {
        BEARER
    }
}
