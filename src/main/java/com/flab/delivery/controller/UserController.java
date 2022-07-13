package com.flab.delivery.controller;

import com.flab.delivery.annotation.hasAuthorization;
import com.flab.delivery.dto.LoginDto;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.flab.delivery.annotation.hasAuthorization.UserLevel.*;
import static com.flab.delivery.controller.response.HttpStatusResponse.STATUS_CREATED;
import static com.flab.delivery.controller.response.HttpStatusResponse.STATUS_OK;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<HttpStatus> signUp(@RequestBody @Valid SignUpDto signUpDto) {

        userService.signUp(signUpDto);

        return STATUS_CREATED;
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<HttpStatus> existById(@PathVariable String id) {

        userService.checkIdDuplicated(id);

        return STATUS_OK;
    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> login(@RequestBody @Valid LoginDto loginDto) {

        userService.login(loginDto);

        return STATUS_OK;
    }

    @hasAuthorization(level = ALL)
    @DeleteMapping("/logout")
    public ResponseEntity<HttpStatus> logout() {

        userService.logout();

        return STATUS_OK;
    }
}
