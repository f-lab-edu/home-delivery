package com.flab.delivery.controller;

import com.flab.delivery.annotation.HasAuthorization;
import com.flab.delivery.dto.LoginDto;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<HttpStatus> signUp(@RequestBody @Valid SignUpDto signUpDto) {

        userService.signUp(signUpDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<HttpStatus> existById(@PathVariable String id) {

        userService.checkDuplicatedId(id);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> login(@RequestBody @Valid LoginDto loginDto) {

        userService.login(loginDto);

        return ResponseEntity.ok().build();
    }

    @HasAuthorization()
    @DeleteMapping("/logout")
    public ResponseEntity<HttpStatus> logout() {

        userService.logout();

        return ResponseEntity.ok().build();
    }
}
