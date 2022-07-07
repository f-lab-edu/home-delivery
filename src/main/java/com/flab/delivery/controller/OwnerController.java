package com.flab.delivery.controller;

import com.flab.delivery.dto.LoginDto;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.flab.delivery.controller.response.HttpStatusResponse.STATUS_CREATED;
import static com.flab.delivery.controller.response.HttpStatusResponse.STATUS_OK;

@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping
    public ResponseEntity<HttpStatus> signUp(@RequestBody @Valid SignUpDto signUpDto) {

        ownerService.signUp(signUpDto);

        return STATUS_CREATED;
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<HttpStatus> existById(@PathVariable String id) {

        ownerService.checkIdDuplicated(id);

        return STATUS_OK;
    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> login(@RequestBody @Valid LoginDto loginDto) {

        ownerService.login(loginDto);

        return STATUS_OK;
    }

    @DeleteMapping("/logout")
    public ResponseEntity<HttpStatus> logout() {

        ownerService.logout();

        return STATUS_OK;
    }

}
