package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.annotation.SessionUserId;
import com.flab.delivery.controller.validator.PasswordValidator;
import com.flab.delivery.dao.FCMTokenDao;
import com.flab.delivery.dto.user.*;
import com.flab.delivery.response.CommonResult;
import com.flab.delivery.service.FCMService;
import com.flab.delivery.service.LoginService;
import com.flab.delivery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final LoginService loginService;
    private final PasswordValidator passwordValidator;
    private final FCMService fcmService;


    @InitBinder("passwordDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(passwordValidator);
    }

    @PostMapping
    public CommonResult<Void> createUser(@RequestBody @Valid SignUpDto signUpDto) {
        userService.createUser(signUpDto);
        return CommonResult.getSimpleSuccessResult(HttpStatus.CREATED.value());
    }

    @PostMapping("/login")
    public CommonResult<Void> loginUser(@RequestBody UserDto userDto) {
        userService.loginUser(userDto);
        fcmService.saveToken(userDto.getId(), userDto.getToken());
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }


    @LoginCheck
    @DeleteMapping("/logout")
    public CommonResult<Void> logoutUser(@SessionUserId String userId) {
        loginService.logoutUser();
        fcmService.deleteToken(userId);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    @LoginCheck
    @GetMapping
    public CommonResult<UserInfoDto> getUserInfo(@SessionUserId String userId) {
        UserInfoDto userInfo = userService.getUserInfo(userId);

        return CommonResult.getDataSuccessResult(userInfo);

    }

    @LoginCheck
    @PatchMapping
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

    @LoginCheck
    @PatchMapping("/password")
    public CommonResult<Void> changePassword(@SessionUserId String userId,
                                             @RequestBody @Valid PasswordDto passwordDto) {
        userService.changePassword(userId, passwordDto);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }
}
