package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.service.LoginService;
import com.flab.delivery.service.UserService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Void> createUser(@RequestBody @Valid final SignUpDto signUpDto) {
        userService.createUser(signUpDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestBody final UserDto userDto) {
        userService.loginUser(userDto);
        return ResponseEntity.ok().build();
    }

    // 로그아웃은 로그인된 상태만 가능하다
    @LoginCheck
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logoutUser() {
        loginService.logoutUser();
        return ResponseEntity.ok().build();
    }


}
