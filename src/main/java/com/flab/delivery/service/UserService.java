package com.flab.delivery.service;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.exception.UserException;
import com.flab.delivery.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;


    public void signUp(SignUpDto signUpDto) {

        if (userMapper.existUserById(signUpDto.getId())) {
            log.error("유저 회원가입 이미 존재하는 아이디 =  {} ", signUpDto.getId());
            throw new UserException("이미 존재하는 아이디 입니다.");
        }

        UserDto userDto = signUpDto.toUserDto();
        userMapper.save(userDto);
    }
}

