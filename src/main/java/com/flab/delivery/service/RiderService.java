package com.flab.delivery.service;

import com.flab.delivery.dto.LoginDto;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.mapper.RiderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiderService {

    private final RiderMapper riderMapper;
    private final CommonService commonService;

    public void signUp(SignUpDto signUpDto) {
        commonService.signUp(riderMapper, signUpDto);
    }

    public void checkIdDuplicated(String id) {
        commonService.checkIdDuplicated(riderMapper, id);
    }

    public void login(LoginDto loginDto) {
        commonService.login(riderMapper, loginDto);
    }
}
