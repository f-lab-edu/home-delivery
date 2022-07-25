package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.annotation.SessionUserId;
import com.flab.delivery.dto.address.AddressRequestDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.response.CommonResult;
import com.flab.delivery.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    @LoginCheck(userType = UserType.USER)
    public CommonResult<Void> addAddress(@RequestBody @Valid AddressRequestDto addressRequestDto,
                                         @SessionUserId String userId) {

        addressService.addAddress(addressRequestDto, userId);

        return CommonResult.getSimpleSuccessResult(HttpStatus.CREATED.value());
    }

}
