package com.flab.delivery.service;

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
    public void createUser(UserDto userDto) {
        if(!userMapper.isExistsId(userDto.getId())){
            throw new SignUpException("이미 존재하는 아이디입니다");
        }
        UserDto insertUser = UserDto.builder()
                .id(userDto.getId())
                .password(PasswordEncoder.encrypt(userDto.getPassword()))
                .email(userDto.getEmail())
                .name(userDto.getName())
                .phoneNumber(userDto.getPhoneNumber())
                .level(userDto.getLevel())
                .build();
        userMapper.insertUser(insertUser);
    }
}
