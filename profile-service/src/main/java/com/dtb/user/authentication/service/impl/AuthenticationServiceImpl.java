package com.dtb.user.authentication.service.impl;

import com.dtb.common.util.security.TokenProvider;
import com.dtb.user.authentication.model.dto.TokenPair;
import com.dtb.user.authentication.repository.UserRepository;
import com.dtb.user.authentication.repository.jpa.UserEntity;
import com.dtb.user.authentication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenPair login(String email, String password) {
        final UserEntity userEntity = userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElseThrow(() -> new AuthorizationDeniedException("Invalid email or password"));
        return getTokenPair(userEntity.getUniqueId(), Collections.singletonList(userEntity.getRole().name()));
    }

    private TokenPair getTokenPair(String userId, List<String> roles) {
        log.info("Bearer token has been created");
        final String accessToken = tokenProvider.createJwt(userId, roles);
        return TokenPair.builder()
                .token(accessToken)
                .expiresIn(tokenProvider.getTokenValidity())
                .build();
    }

}