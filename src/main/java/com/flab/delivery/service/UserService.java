package com.flab.delivery.service;

import com.flab.delivery.dto.LoginDto;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.exception.PasswordException;
import com.flab.delivery.exception.UserException;
import com.flab.delivery.mapper.UserMapper;
import com.flab.delivery.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final LoginService loginService;

    public void signUp(SignUpDto signUpDto) {

        signUpDto.setPassword(passwordEncoder.encoder(signUpDto.getPassword()));

        mapper.save(signUpDto);
    }

    public void checkIdDuplicated(String id) {
        if (mapper.existsUserById(id)) {
            log.error("유저 회원가입 이미 존재하는 아이디 =  {} ", id);
            throw new UserException("이미 존재하는 아이디 입니다.");
        }
    }

    public void login(LoginDto loginDto) {

        UserDto findMember = mapper
                .findUserById(loginDto.getId())
                .orElseThrow(() -> {
                    log.error("해당 회원이 존재하지 않음 id = {}", loginDto.getId());
                    throw new UserException("해당 회원을 찾을 수 없습니다.");
                });

        if (!passwordEncoder.isMatch(loginDto.getPassword(), findMember.getPassword())) {
            throw new PasswordException("비밀번호가 일치하지 않습니다.");
        }

        loginService.login(findMember.getId());
    }

    public void logout() {
        if (loginService.getCurrentUserId() == null) {
            throw new UserException("로그인 하지 않은 사용자 입니다.");
        }
        loginService.logout();
    }
}
