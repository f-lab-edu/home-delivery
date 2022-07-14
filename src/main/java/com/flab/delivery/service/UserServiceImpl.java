package com.flab.delivery.service;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.exception.LoginException;
import com.flab.delivery.exception.SignUpException;
import com.flab.delivery.mapper.UserMapper;
import com.flab.delivery.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final LoginService loginService;


    @Override
    public void createUser(final SignUpDto signUpDto) {
        if (userMapper.isExistsId(signUpDto.getId())) {
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


    // 인증 과정
    // 아이디가 존재하는지 찾는다
    // 비밀번호가 맞는지 찾는다(아이디랑 비밀번호를 확인해주세요)
    // "예외 메시지를 던진다" 예외를 처리 한다
    @Override
    public void loginUser(final UserDto userDto) {
        if (!userMapper.isExistsId(userDto.getId())) {
            throw new LoginException("존재하지 않는 아이디입니다");
        }

        UserDto findUser = userMapper.findById(userDto.getId());
        if (!PasswordEncoder.isMatch(userDto.getPassword(), findUser.getPassword())) {
            throw new LoginException("아이디랑 비밀번호를 확인해주세요");
        }
        loginService.loginUser(userDto.getId());
    }
}
