package com.dtb.common.util.model;

import lombok.Builder;

@Builder
public record ErrorResponse(
    String message
){}