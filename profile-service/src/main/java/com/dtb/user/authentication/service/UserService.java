package com.dtb.user.authentication.service;

import com.dtb.user.authentication.model.dto.UserDto;
import com.dtb.user.authentication.model.dto.request.UserCredentialsRequest;

public interface UserService {
    UserDto createUser(UserCredentialsRequest accountRequest);
}
