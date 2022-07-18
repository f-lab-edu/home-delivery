package com.flab.delivery.service;

import com.flab.delivery.dto.LoginDto;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.exception.AuthException;
import com.flab.delivery.exception.BadInputException;
import com.flab.delivery.exception.UserException;
import com.flab.delivery.mapper.UserMapper;
import com.flab.delivery.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper mapper;
    private final LoginService loginService;

    public void signUp(SignUpDto signUpDto) {

        signUpDto.setPassword(PasswordEncoder.encode(signUpDto.getPassword()));

        mapper.save(signUpDto);
    }

    public void checkDuplicatedId(String id) {
        if (mapper.hasUserById(id)) {
            log.error("유저 회원가입 이미 존재하는 아이디 =  {} ", id);
            throw new BadInputException("이미 존재하는 아이디 입니다.");
        }
    }

    public void login(LoginDto loginDto) {

        UserDto findMember = mapper
                .findUserById(loginDto.getId())
                .orElseThrow(() -> {
                    log.error("해당 회원이 존재하지 않음 id = {}", loginDto.getId());
                    throw new UserException("해당 회원을 찾을 수 없습니다.");
                });

        if (!PasswordEncoder.matches(loginDto.getPassword(), findMember.getPassword())) {
            throw new AuthException("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        loginService.login(findMember.getId());
    }

    public void logout() {
        loginService.logout();
    }
}
