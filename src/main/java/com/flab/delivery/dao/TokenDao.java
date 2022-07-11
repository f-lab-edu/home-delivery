package com.flab.delivery.dao;

import com.flab.delivery.dto.TokenDto;

public interface TokenDao {
    TokenDto save(String id, TokenDto token);

    void remove(String id);

    TokenDto getTokenByUserId(String id);
}
