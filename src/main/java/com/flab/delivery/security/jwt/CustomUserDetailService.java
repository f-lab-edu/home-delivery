package com.flab.delivery.security.jwt;

import com.flab.delivery.dto.UserDto.AuthDto;
import com.flab.delivery.exception.AuthException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
            throw new AuthException("권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        return new CustomUser(
                AuthDto
                        .builder()
                        .id((String) claims.get("sub"))
                        .level((String) claims.get("level"))
                        .build());
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = loadUserByUsername(token);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
