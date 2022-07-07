package com.flab.delivery.service;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.mapper.OwnerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerMapper ownerMapper;
    private final CommonService commonService;

    public void signUp(SignUpDto signUpDto) {
        commonService.signUp(ownerMapper, signUpDto);
    }

    public void checkIdDuplicated(String id) {
        commonService.checkIdDuplicated(ownerMapper, id);
    }
}
