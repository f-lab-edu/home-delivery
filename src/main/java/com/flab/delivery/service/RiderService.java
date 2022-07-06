package com.flab.delivery.service;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.mapper.RiderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiderService {

    private final RiderMapper riderMapper;
    private final SignUpService signUpService;

    public void signUp(SignUpDto signUpDto) {
        signUpService.signUp(riderMapper, signUpDto);
    }
}
