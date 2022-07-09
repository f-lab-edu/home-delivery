package com.flab.delivery.security.jwt;

import com.flab.delivery.dto.UserDto;
import com.flab.delivery.exception.CertifyException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final JwtProvider jwtProvider;
    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {

        Claims claims = jwtProvider.parseClaims(token);

        if (claims.get("level") == null) {
            throw new CertifyException("권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        return new CustomUser(UserDto.LoginUserDto.builder()
                .id((String) claims.get("id"))
                .level((String) claims.get("level")));
    }
}
