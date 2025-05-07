package com.dtb.common.util.security.impl;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dtb.common.util.config.CommonUtilProperties;
import com.dtb.common.util.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@RequiredArgsConstructor
@Component
public class TokenProviderImpl implements TokenProvider {
    private static final String ROLES = "roles";
    private final CommonUtilProperties properties;

    public String createJwt(String userId, List<String> roles) {
        return JWT.create()
                .withSubject(userId)
                .withArrayClaim(ROLES, roles.toArray(new String[0]))
                .withExpiresAt(new Date(System.currentTimeMillis() + properties.getJwtToken().getValidity() * 1000))
                .sign(HMAC512(this.properties.getJwtToken().getSecretKey()));
    }

    public User createUser(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC512(properties.getJwtToken().getSecretKey()))
                .build()
                .verify(token);

        List<SimpleGrantedAuthority> roles = jwt.getClaim(ROLES).asList(String.class).stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new User(jwt.getSubject(), "", roles);
    }

    public Integer getTokenValidity() {
        return properties.getJwtToken().getValidity();
    }
}