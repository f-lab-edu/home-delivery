package com.flab.delivery.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RiderDao {

    private final RedisTemplate<String, Object> redisTemplate;


    public void registerStandByRider(String userId, Long addressId) {
        redisTemplate.opsForSet().add(getRidersKeyBy(addressId), userId);
        redisTemplate.expire(getRidersKeyBy(addressId), 1, TimeUnit.DAYS);
        return;
    }

    public Set<Object> findAllRiderBy(Long addressId) {
        return redisTemplate.opsForSet().members(getRidersKeyBy(addressId));
    }

    public void deleteStandByRider(String userId, Long addressId) {
        redisTemplate.opsForSet().remove(getRidersKeyBy(addressId), userId);
        return;
    }

    private String getRidersKeyBy(Long addressId) {
        return "STANDBY_RIDER" + addressId;
    }
}
