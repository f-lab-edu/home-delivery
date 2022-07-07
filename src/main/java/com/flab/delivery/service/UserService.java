package com.flab.delivery.service;

import com.flab.delivery.dto.LoginDto;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final CommonService commonService;

    public void signUp(SignUpDto signUpDto) {
        commonService.signUp(userMapper, signUpDto);
    }

    public void checkIdDuplicated(String id) {
        commonService.checkIdDuplicated(userMapper, id);
    }

    public void login(LoginDto loginDto) {
        commonService.login(userMapper, loginDto);
    }

    public void logout() {
        commonService.logout();
    }
}

