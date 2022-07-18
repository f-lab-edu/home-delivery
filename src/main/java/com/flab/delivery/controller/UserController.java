package com.flab.delivery.controller;

import com.flab.delivery.annotation.HasAuthorization;
import com.flab.delivery.controller.reponse.CommonResult;
import com.flab.delivery.dto.LoginDto;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public CommonResult signUp(@RequestBody @Valid SignUpDto signUpDto) {

        userService.signUp(signUpDto);

        return CommonResult.getSimpleSuccessResult(HttpStatus.CREATED.value());
    }

    @GetMapping("/{id}/exists")
    public CommonResult existById(@PathVariable String id) {

        userService.checkDuplicatedId(id);

        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    @PostMapping("/login")
    public CommonResult login(@RequestBody @Valid LoginDto loginDto) {

        userService.login(loginDto);

        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    @HasAuthorization()
    @DeleteMapping("/logout")
    public CommonResult logout() {

        userService.logout();

        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }
}
