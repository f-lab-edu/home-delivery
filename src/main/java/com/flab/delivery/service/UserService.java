package com.flab.delivery.service;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.UserDto;

public interface UserService {

    void createUser(SignUpDto signUpDto);

    void loginUser(UserDto userDto);


}
