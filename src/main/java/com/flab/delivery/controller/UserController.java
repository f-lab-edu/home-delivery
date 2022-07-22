package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.annotation.SessionUserId;
import com.flab.delivery.dto.user.SignUpDto;
import com.flab.delivery.dto.user.UserDto;
import com.flab.delivery.dto.user.UserInfoDto;
import com.flab.delivery.dto.user.UserInfoUpdateDto;
import com.flab.delivery.response.CommonResult;
import com.flab.delivery.service.LoginService;
import com.flab.delivery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @LoginCheck
    @PutMapping
    public CommonResult<Void> updateUserInfo(@SessionUserId String userId,
                                             @RequestBody @Valid UserInfoUpdateDto userInfoUpdateDto) {

        userService.updateUserInfo(userId, userInfoUpdateDto);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    @LoginCheck
    @DeleteMapping
    public CommonResult<Void> deleteUser(@SessionUserId String userId) {
        userService.deleteUser(userId);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }
}
