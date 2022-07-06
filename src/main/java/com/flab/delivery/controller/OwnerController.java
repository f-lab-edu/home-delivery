package com.flab.delivery.controller;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpDto signUpDto) {

        ownerService.signUp(signUpDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
