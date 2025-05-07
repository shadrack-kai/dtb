package com.dtb.common.util.security;

import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface TokenProvider {
    String createJwt(String userId, List<String> roles);
    User createUser(String token);
    Integer getTokenValidity();
}
