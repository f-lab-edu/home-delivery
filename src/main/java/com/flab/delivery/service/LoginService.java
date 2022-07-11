package com.flab.delivery.service;

import com.flab.delivery.dto.UserDto;

public interface LoginService {

    void loginUser(String id);

    String getSessionUserId();

    void logoutUser();
}
