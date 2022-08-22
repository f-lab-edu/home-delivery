package com.flab.delivery.dao;

import com.flab.delivery.AbstractRedisContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RiderDaoTestRedis extends AbstractRedisContainer {

    private static final String RIDER = "Rider1";
    private static final Long ADDRESS_ID = 1L;
    private static final Long ORDER_ID = 1L;
    @Autowired
    private RiderDao riderDao;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void init() {
        redisTemplate.opsForSet().remove(getKey("RIDER"), RIDER);
        redisTemplate.opsForZSet().remove(getKey("ORDER"), ORDER_ID);
    }
    @Test
    void registerStandByRider_확인() {
        // given
        riderDao.registerStandByRider(RIDER, ADDRESS_ID);

        // when
        Set<Object> riders = getRiders();
        // then
        assertThat(riders.contains(RIDER)).isTrue();
    }

    private Set<Object> getRiders() {
        return redisTemplate.opsForSet().members(getKey("RIDER"));
    }


    @Test
    void deleteStandByRider_확인() {
        // given
        riderDao.registerStandByRider(RIDER, ADDRESS_ID);

        // when
        Set<Object> riders = getRiders();
        assertThat(riders.contains(RIDER)).isTrue();
        riderDao.deleteStandByRider(RIDER, ADDRESS_ID);

        // then
        riders = getRiders();
        assertThat(riders.contains(RIDER)).isFalse();
    }

    @Test
    void addOrderBy_첫번째_요청으로_성공() {
        // given
        // when
        boolean isAdded = riderDao.addOrderBy(ADDRESS_ID, ORDER_ID);

        // then
        assertThat(isAdded).isTrue();
    }

    @Test
    void addOrderBy_이미_주문이_있으며_1분이_지나지_않아_False_반환() {
        // given
        riderDao.addOrderBy(ADDRESS_ID, ORDER_ID);

        // when
        boolean isAdded = riderDao.addOrderBy(ADDRESS_ID, ORDER_ID);

        // then
        assertThat(isAdded).isFalse();
    }

    @Test
    void addOrderBy_이미_주문이_있지만_1분이_지나_Score_업데이트() {
        // given
        String OrderKey = getKey("ORDER");
        long beforeScore = System.currentTimeMillis() - 60 * 1000;
        redisTemplate.opsForZSet().add(OrderKey, ORDER_ID, beforeScore);

        // when
        boolean isAdded = riderDao.addOrderBy(ADDRESS_ID, ORDER_ID);

        // then
        assertThat(isAdded).isTrue();
        Double afterScore = redisTemplate.opsForZSet().score(OrderKey, ORDER_ID);
        assertThat(afterScore).isGreaterThan(beforeScore);
    }


    @NotNull
    private String getKey(String key) {
        return key + ADDRESS_ID;
    }
}