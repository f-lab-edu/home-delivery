package com.flab.delivery.service;

import com.flab.delivery.dto.user.*;
import com.flab.delivery.exception.LoginException;
import com.flab.delivery.exception.SessionLoginException;
import com.flab.delivery.exception.SignUpException;
import com.flab.delivery.mapper.UserMapper;
import com.flab.delivery.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.flab.delivery.exception.message.ErrorMessageConstants.FORBIDDEN_MESSAGE;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserMapper userMapper;
    private final LoginService loginService;


    public void createUser(SignUpDto signUpDto) {
        if (userMapper.idExists(signUpDto.getId())) {
            throw new SignUpException("이미 존재하는 아이디입니다");
        }

        SignUpDto saveUser = SignUpDto.encryptUser(signUpDto);
        userMapper.save(saveUser);
    }

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

    public UserInfoDto getUserInfo(String userId) {
        return UserInfoDto.fromUserDto(userMapper.findById(userId));
    }

    public void updateUserInfo(String userId, UserInfoUpdateDto userInfoUpdateDto) {

        if (!userId.equals(userInfoUpdateDto.getId())) {
            throw new SessionLoginException(FORBIDDEN_MESSAGE, HttpStatus.FORBIDDEN);
        }

        userMapper.updateInfo(userInfoUpdateDto);
    }

    public void deleteUser(String userId) {
        userMapper.deleteById(userId);
    }

    public void changePassword(String userId, PasswordDto passwordDto) {
        userMapper.changePassword(userId, PasswordEncoder.encrypt(passwordDto.getNewPassword()));
    }
}
