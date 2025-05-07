package com.dtb.user.authentication.service;

import com.dtb.user.authentication.model.dto.TokenPair;

public interface AuthenticationService {
    TokenPair login(String email, String password);
}
