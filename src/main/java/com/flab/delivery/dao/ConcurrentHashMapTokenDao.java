package com.flab.delivery.dao;

import com.flab.delivery.dto.TokenDto;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ConcurrentHashMapTokenDao implements TokenDao {

    private final ConcurrentHashMap<String, TokenDto> tokenMap = new ConcurrentHashMap<>();

    @Override
    public TokenDto save(String id, TokenDto token) {
        tokenMap.put(id, token);

        return token;
    }

    @Override
    public void remove(String id) {
        tokenMap.remove(id);
    }

    @Override
    public TokenDto getTokenByUserId(String id) {
        return tokenMap.get(id);
    }
}
