package com.flab.delivery.dao;

import com.flab.delivery.AbstractRedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RiderDaoTestRedis extends AbstractRedisContainer {

    private static final String RIDER = "Rider1";
    public static final long ADDRESS_ID = 1L;
    @Autowired
    private RiderDao riderDao;

    @Test
    void registerStandByRider_확인() {
        // given
        riderDao.registerStandByRider(RIDER, ADDRESS_ID);

        // when
        Set<Object> riders = riderDao.findAllRiderBy(ADDRESS_ID);

        // then
        assertThat(riders.contains(RIDER)).isTrue();
    }


    @Test
    void deleteStandByRider_확인() {
        // given
        riderDao.registerStandByRider(RIDER, ADDRESS_ID);

        // when
        Set<Object> riders = riderDao.findAllRiderBy(ADDRESS_ID);
        assertThat(riders.contains(RIDER)).isTrue();
        riderDao.deleteStandByRider(RIDER, ADDRESS_ID);

        // then
        riders = riderDao.findAllRiderBy(ADDRESS_ID);
        assertThat(riders.contains(RIDER)).isFalse();
    }
}