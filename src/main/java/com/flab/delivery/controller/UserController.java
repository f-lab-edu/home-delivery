package com.flab.delivery.controller;

import com.flab.delivery.dto.LoginDto;
import com.flab.delivery.dto.SignUpDto;
import com.flab.delivery.dto.TokenDto;
import com.flab.delivery.dto.UserDto.AuthDto;
import com.flab.delivery.exception.AuthException;
import com.flab.delivery.security.jwt.CurrentUser;
import com.flab.delivery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginDto loginDto) {

        TokenDto tokenDto = userService.login(loginDto);

        return new ResponseEntity<>(tokenDto, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/logout")
    public ResponseEntity<HttpStatus> logout(@CurrentUser AuthDto authDto) {

        if (authDto == null) {
            throw new AuthException("로그인 되지 않은 사용자 입니다.", HttpStatus.UNAUTHORIZED);
        }

        userService.logout(authDto.getId());

        return STATUS_OK;
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenDto tokenDto) {

        TokenDto newTokenDto = userService.reissue(tokenDto.getAccessToken(), tokenDto.getRefreshToken());

        return new ResponseEntity<>(newTokenDto, HttpStatus.OK);
    }
}
