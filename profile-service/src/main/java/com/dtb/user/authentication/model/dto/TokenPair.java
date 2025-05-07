package com.dtb.user.authentication.model.dto;

import lombok.Builder;

@Builder
public record TokenPair (
    String token,
    String type,
    Integer expiresIn
){}
