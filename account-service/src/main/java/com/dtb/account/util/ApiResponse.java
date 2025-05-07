package com.dtb.account.util;

import lombok.Builder;

@Builder
public record ApiResponse(
        String message,
        Object payload
) {}