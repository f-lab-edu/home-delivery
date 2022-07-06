package com.flab.delivery.controller;

import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/riders")
@RequiredArgsConstructor
public class RiderController {
    private final RiderService riderService;

    @PostMapping
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpDto signUpDto) {

        riderService.signUp(signUpDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
