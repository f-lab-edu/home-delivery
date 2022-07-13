package com.flab.delivery.service;

import com.flab.delivery.dao.TokenDao;
import com.flab.delivery.dto.TokenDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.dto.UserDto.AuthDto;
import com.flab.delivery.exception.AuthorizationException;
import com.flab.delivery.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtLoginService implements LoginService {

    private final JwtProvider jwtProvider;
    private final TokenDao tokenDao;

    @Override
    public TokenDto login(UserDto userDto) {
        TokenDto token = jwtProvider.createToken(userDto);
        return tokenDao.save(userDto.getId(), token);
    }

    @Override
    public void logout(String id) {
        tokenDao.removeTokenByUserId(id);
    }

    @Override
    public TokenDto reissue(String accessToken, String refreshToken) {

        jwtProvider.validateTokenToReissue(tokenDao, accessToken, refreshToken);

        AuthDto authDto = jwtProvider.getAuthDto(refreshToken);

        String userId = authDto.getId();
        String level = authDto.getLevel();

        TokenDto findTokenDto = tokenDao.getTokenByUserId(userId)
                .orElseThrow(() -> new AuthorizationException("토큰을 찾을 수 없습니다.", HttpStatus.CONFLICT));

        if (!findTokenDto.getRefreshToken().equals(refreshToken)) {
            throw new AuthorizationException("토큰 값이 옳바르지 않습니다.", HttpStatus.FORBIDDEN);
        }

        return tokenDao.save(userId, jwtProvider.createToken(userId, level));
    }
}
