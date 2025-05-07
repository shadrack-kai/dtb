package com.dtb.common.util.config;

import com.dtb.common.util.security.SecurityAuthenticationFilter;
import com.dtb.common.util.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final TokenProvider tokenProvider;
    private final CommonUtilProperties properties;

    @Bean
    public SecurityAuthenticationFilter securityAuthenticationFilter() {
        return new SecurityAuthenticationFilter(tokenProvider);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers(createRequestMatchers())
                        .permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(securityAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private String[] createRequestMatchers() {
        List<String> urls = new ArrayList<>();
        urls.add("/actuator/**");
        urls.add("/csrf");
        urls.add("/v3/api-docs/**");
        urls.add("/swagger-ui/**");

        if(StringUtils.hasText(properties.getRequestMatchers()))
            urls.addAll(Arrays.asList(properties.getRequestMatchers().split(",")));
        return urls.toArray(new String[0]);
    }
}
