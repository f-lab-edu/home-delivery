package com.flab.delivery.service;

import com.flab.delivery.dto.UserDto;
import com.flab.delivery.enums.UserType;

public interface LoginService {

    void loginUser(UserDto userDto);

    String getSessionUserId();

    UserType getUserType();

    void logoutUser();
}
