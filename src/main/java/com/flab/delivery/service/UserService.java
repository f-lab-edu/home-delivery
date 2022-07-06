package com.flab.delivery.service;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final SignUpService signUpService;

    public void signUp(SignUpDto signUpDto) {
        signUpService.signUp(userMapper, signUpDto);
    }

    public void checkIdDuplicated(String id) {
        signUpService.checkIdDuplicated(userMapper, id);
    }
}

