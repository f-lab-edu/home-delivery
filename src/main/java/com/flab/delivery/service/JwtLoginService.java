package com.flab.delivery.service;

import com.flab.delivery.dao.TokenDao;
import com.flab.delivery.dto.TokenDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        tokenDao.remove(id);
    }
}
