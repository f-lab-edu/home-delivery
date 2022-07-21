package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.annotation.SessionUserId;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.dto.UserInfoDto;
import com.flab.delivery.mapper.UserMapper;
import com.flab.delivery.response.CommonResult;
import com.flab.delivery.service.LoginService;
import com.flab.delivery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final LoginService loginService;

    @PostMapping
    public CommonResult<Void> createUser(@RequestBody @Valid SignUpDto signUpDto) {
        userService.createUser(signUpDto);
        return CommonResult.getSimpleSuccessResult(HttpStatus.CREATED.value());
    }

    @PostMapping("/login")
    public CommonResult<Void> loginUser(@RequestBody UserDto userDto) {
        userService.loginUser(userDto);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }


    @LoginCheck
    @DeleteMapping("/logout")
    public CommonResult<Void> logoutUser() {
        loginService.logoutUser();
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    @LoginCheck
    @GetMapping
    public CommonResult<UserInfoDto> getUserInfo(@SessionUserId String userId) {
        UserInfoDto userInfo = userService.getUserInfo(userId);

        return CommonResult.getDataSuccessResult(userInfo);

    }

}
