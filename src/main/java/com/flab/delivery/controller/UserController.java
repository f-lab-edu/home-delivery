package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.annotation.SessionUserId;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.enums.UserLevel;
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

    @PostMapping("/signup")
    public ResponseEntity createUser(@RequestBody @Valid SignUpDto signUpDto){
        userService.createUser(signUpDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody UserDto userDto){
        userService.loginUser(userDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 로그아웃은 로그인된 상태만 가능하다
    @LoginCheck(userLevel = UserLevel.ALL)
    @DeleteMapping("/logout")
    public ResponseEntity logoutUser(){
        loginService.logoutUser();
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
