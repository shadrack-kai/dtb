package com.dtb.account.util;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String message
) {}
