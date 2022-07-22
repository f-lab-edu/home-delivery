package com.flab.delivery.service;

import com.flab.delivery.dto.user.SignUpDto;
import com.flab.delivery.dto.user.UserDto;
import com.flab.delivery.dto.user.UserInfoDto;
import com.flab.delivery.dto.user.UserInfoUpdateDto;

public interface UserService {

    void createUser(SignUpDto signUpDto);

    void loginUser(UserDto userDto);

    UserInfoDto getUserInfo(String userId);

    void updateUserInfo(String userId, UserInfoUpdateDto userInfoUpdateDto);
}
