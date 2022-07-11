package com.flab.delivery.controller;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.service.LoginService;
import com.flab.delivery.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
//    private final LoginService loginService;

    @PostMapping("/signup")
    public ResponseEntity createUser(@RequestBody @Valid SignUpDto signUpDto){
        userService.createUser(signUpDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 세션x 처음 로그인임
    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody UserDto userDto){
        userService.loginUser(userDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 로그아웃은 로그인된 상태이다
    // @ 어노테이션으로 AOP적용할 것 정하고
    // 매개변수로 아규먼트 메소드 바인딩 해주기
    @DeleteMapping("/logout")
    public ResponseEntity logoutUser(String id){

    }






}
