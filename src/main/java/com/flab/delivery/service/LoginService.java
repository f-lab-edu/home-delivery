package com.flab.delivery.service;

import com.flab.delivery.dto.TokenDto;
import com.flab.delivery.dto.UserDto;

public interface LoginService {

    TokenDto login(UserDto userDto);

    void logout(String id);

    TokenDto reissue(String accessToken, String refreshToken);
}
