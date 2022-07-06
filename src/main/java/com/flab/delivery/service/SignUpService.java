package com.flab.delivery.service;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.exception.SignUpException;
import com.flab.delivery.mapper.CommonMapper;
import com.flab.delivery.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpService {

    private final PasswordEncoder passwordEncoder;

    public void signUp(CommonMapper mapper, SignUpDto signUpDto) {

        if (mapper.existById(signUpDto.getId())) {
            log.error("유저 회원가입 이미 존재하는 아이디 =  {} ", signUpDto.getId());
            throw new SignUpException("이미 존재하는 아이디 입니다.");
        }
        signUpDto.setPassword(passwordEncoder.encoder(signUpDto.getPassword()));

        mapper.save(signUpDto);
    }
}
