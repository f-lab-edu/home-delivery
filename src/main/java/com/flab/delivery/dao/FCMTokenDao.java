package com.flab.delivery.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FCMTokenDao {

    private final StringRedisTemplate redisFCMTokenTemplate;

    public void save(String userId, String token) {
        redisFCMTokenTemplate.opsForValue().set(userId, token);
    }

    public void delete(String userId) {
        redisFCMTokenTemplate.delete(userId);
    }

    public String findToken(String userId) {
        return redisFCMTokenTemplate.opsForValue().get(userId);
    }


}
