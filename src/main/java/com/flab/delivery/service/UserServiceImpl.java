package com.flab.delivery.service;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.exception.SignUpException;
import com.flab.delivery.mapper.UserMapper;
import com.flab.delivery.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;

    @Override
    public void createUser(SignUpDto signUpDto) {
        if(userMapper.isExistsId(signUpDto.getId())){
            throw new SignUpException("이미 존재하는 아이디입니다");
        }
        SignUpDto insertUser = SignUpDto.builder()
                .id(signUpDto.getId())
                .password(PasswordEncoder.encrypt(signUpDto.getPassword()))
                .email(signUpDto.getEmail())
                .name(signUpDto.getName())
                .phoneNumber(signUpDto.getPhoneNumber())
                .level(signUpDto.getLevel())
                .build();
        userMapper.insertUser(insertUser);
    }
}