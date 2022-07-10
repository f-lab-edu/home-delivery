package com.flab.delivery.service;

import com.flab.delivery.dto.UserDto;
import com.flab.delivery.dto.UserDto.LoginUserDto;

public interface LoginService {

    void login(UserDto userDto);

    void logout();

    LoginUserDto getCurrentUser();
}
