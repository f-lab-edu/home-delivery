package com.flab.delivery.controller;

import com.flab.delivery.dto.LoginDto;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.flab.delivery.controller.response.HttpStatusResponse.STATUS_CREATED;
import static com.flab.delivery.controller.response.HttpStatusResponse.STATUS_OK;

@RestController
@RequestMapping("/riders")
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;

    @PostMapping
    public ResponseEntity<HttpStatus> signUp(@RequestBody @Valid SignUpDto signUpDto) {

        riderService.signUp(signUpDto);

        return STATUS_CREATED;
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<HttpStatus> existById(@PathVariable String id) {

        riderService.checkIdDuplicated(id);

        return STATUS_OK;
    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> login(@RequestBody @Valid LoginDto loginDto) {

        riderService.login(loginDto);

        return STATUS_OK;
    }

    @DeleteMapping("/logout")
    public ResponseEntity<HttpStatus> logout() {

        riderService.logout();

        return STATUS_OK;
    }
}
