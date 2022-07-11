package com.flab.delivery.dao;

import com.flab.delivery.dto.TokenDto;

import java.util.Optional;

public interface TokenDao {
    TokenDto save(String id, TokenDto token);

    void removeTokenByUserId(String id);

    Optional<TokenDto> getTokenByUserId(String id);
}
