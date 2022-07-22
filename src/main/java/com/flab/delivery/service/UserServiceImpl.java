package com.flab.delivery.service;

import com.flab.delivery.dto.user.SignUpDto;
import com.flab.delivery.dto.user.UserDto;
import com.flab.delivery.dto.user.UserInfoDto;
import com.flab.delivery.dto.user.UserInfoUpdateDto;
import com.flab.delivery.exception.LoginException;
import com.flab.delivery.exception.SessionLoginException;
import com.flab.delivery.exception.SignUpException;
import com.flab.delivery.mapper.UserMapper;
import com.flab.delivery.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final LoginService loginService;


    @Override
    public void createUser(SignUpDto signUpDto) {
        if (userMapper.idExists(signUpDto.getId())) {
            throw new SignUpException("이미 존재하는 아이디입니다");
        }
        SignUpDto saveUser = SignUpDto.builder()
                .id(signUpDto.getId())
                .password(PasswordEncoder.encrypt(signUpDto.getPassword()))
                .email(signUpDto.getEmail())
                .name(signUpDto.getName())
                .phoneNumber(signUpDto.getPhoneNumber())
                .type(signUpDto.getType())
                .build();
        userMapper.save(saveUser);
    }

    @Override
    public void loginUser(UserDto userDto) {
        if (!userMapper.idExists(userDto.getId())) {
            throw new LoginException("존재하지 않는 아이디입니다");
        }

        UserDto findUser = userMapper.findById(userDto.getId());
        if (!PasswordEncoder.matches(userDto.getPassword(), findUser.getPassword())) {
            throw new LoginException("아이디랑 비밀번호를 확인해주세요");
        }
        loginService.loginUser(findUser);
    }

    @Override
    public UserInfoDto getUserInfo(String userId) {
        return UserInfoDto.fromUserDto(userMapper.findById(userId));
    }

    @Override
    public void updateUserInfo(String userId, UserInfoUpdateDto userInfoUpdateDto) {

        if (!userId.equals(userInfoUpdateDto.getId())) {
            throw new SessionLoginException("권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        userMapper.updateInfo(userInfoUpdateDto);
    }
}
