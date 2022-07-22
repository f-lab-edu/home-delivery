package com.flab.delivery.service;

import com.flab.delivery.dto.user.*;

public interface UserService {

    void createUser(SignUpDto signUpDto);

    void loginUser(UserDto userDto);

    UserInfoDto getUserInfo(String userId);

    void updateUserInfo(String userId, UserInfoUpdateDto userInfoUpdateDto);

    void deleteUser(String userId);

    void changePassword(String userId, PasswordDto passwordDto);
}
