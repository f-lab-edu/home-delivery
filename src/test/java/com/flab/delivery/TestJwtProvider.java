package com.flab.delivery;

import com.flab.delivery.security.jwt.JwtProvider;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class TestJwtProvider extends JwtProvider {
    @Override
    public boolean isExpiredToken(String token) {
        return true;
    }
}
