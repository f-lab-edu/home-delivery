package com.flab.delivery.service;

import com.flab.delivery.dto.LoginDto;
import com.flab.delivery.dto.MemberDto;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.exception.MemberException;
import com.flab.delivery.exception.PasswordException;
import com.flab.delivery.mapper.CommonMapper;
import com.flab.delivery.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonService {

    private final PasswordEncoder passwordEncoder;
    private final LoginService loginService;

    public void signUp(CommonMapper mapper, SignUpDto signUpDto) {

        signUpDto.setPassword(passwordEncoder.encoder(signUpDto.getPassword()));

        mapper.save(signUpDto);
    }

    public void checkIdDuplicated(CommonMapper mapper, String id) {
        if (mapper.existsById(id)) {
            log.error("유저 회원가입 이미 존재하는 아이디 =  {} ", id);
            throw new MemberException("이미 존재하는 아이디 입니다.");
        }
    }

    public void login(CommonMapper mapper, LoginDto loginDto) {

        MemberDto findMember = mapper
                .findMemberById(loginDto.getId())
                .orElseThrow(() -> {
                    log.error("해당 회원이 존재하지 않음 id = {}", loginDto.getId());
                    throw new MemberException("해당 회원을 찾을 수 없습니다.");
                });

        if (!passwordEncoder.isMatch(loginDto.getPassword(), findMember.getPassword())) {
            throw new PasswordException("비밀번호가 일치하지 않습니다.");
        }

        loginService.login(findMember.getId());
    }
}
