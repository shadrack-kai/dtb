package com.dtb.user.authentication.service.impl;

import com.dtb.user.authentication.repository.UserRepository;
import com.dtb.user.authentication.repository.jpa.UserEntity;
import com.dtb.user.authentication.service.UserService;
import com.dtb.user.authentication.model.dto.UserDto;
import com.dtb.user.authentication.model.dto.request.UserCredentialsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserCredentialsRequest accountRequest) {
        if(!accountRequest.getPassword().equals(accountRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Password do not match");
        }

        var userDetails = createUserEntity(accountRequest);
        var userEntity = userRepository.save(userDetails);
        return UserDto.builder()
                .id(userEntity.getUniqueId())
                .createdAt(userEntity.getCreatedAt())
                .email(accountRequest.getEmail())
                .build();
    }

    private UserEntity createUserEntity(UserCredentialsRequest accountRequest) {
        return UserEntity.builder()
                .uniqueId(UUID.randomUUID().toString())
                .email(accountRequest.getEmail())
                .password(passwordEncoder.encode(accountRequest.getPassword()))
                .build();
    }
}