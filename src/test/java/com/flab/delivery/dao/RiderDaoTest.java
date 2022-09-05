package com.flab.delivery.dao;

import com.flab.delivery.annotation.IntegrationTest;
import com.flab.delivery.dto.order.rider.OrderDeliveryDto;
import com.flab.delivery.exception.OrderException;
import com.flab.delivery.fixture.TestDto;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Set;

import static com.flab.delivery.fixture.TestDto.getOrderDeliveryDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
class RiderDaoTest {

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
        redisTemplate.opsForZSet().removeRange(getKey("ORDER"), 0, -1);
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
    void addDeliveryRequestBy_첫번째_요청으로_성공() {
        // given
        // when
        // then
        riderDao.addDeliveryRequestBy(ADDRESS_ID, getOrderDeliveryDto(ORDER_ID));
    }

    @Test
    void addDeliveryRequestBy_이미_주문이_있으며_1분이_지나지_않아_False_반환() {
        // given
        riderDao.addDeliveryRequestBy(ADDRESS_ID, getOrderDeliveryDto(ORDER_ID));

        // when
        // then
        assertThatThrownBy(() -> riderDao.addDeliveryRequestBy(ADDRESS_ID, getOrderDeliveryDto(ORDER_ID)))
                .isInstanceOf(OrderException.class);

    }

    @Test
    void addDeliveryRequestBy_이미_주문이_있지만_1분이_지나_Score_업데이트() {
        // given
        String OrderKey = getKey("ORDER");
        long beforeScore = System.currentTimeMillis() - 60 * 1000;
        redisTemplate.opsForZSet().add(OrderKey, ORDER_ID, beforeScore);

        // when
        riderDao.addDeliveryRequestBy(ADDRESS_ID, getOrderDeliveryDto(ORDER_ID));

        // then
        Double afterScore = redisTemplate.opsForZSet().score(OrderKey, getOrderDeliveryDto(ORDER_ID));
        assertThat(afterScore).isGreaterThan(beforeScore);
    }

    @Test
    void getDeliveryRequestList_30개_미만_확인() {
        // given
        for (int i = 0; i < 10; i++) {
            riderDao.addDeliveryRequestBy(ADDRESS_ID, TestDto.getOrderDeliveryDto(ORDER_ID + i));
        }

        // when
        List<OrderDeliveryDto> requestList = riderDao.getDeliveryRequestList(ADDRESS_ID);

        // then
        assertThat(requestList.size()).isEqualTo(10);
    }


    @Test
    void getDeliveryRequestList_30개_초과_확인() {
        // given
        for (int i = 0; i < 40; i++) {
            riderDao.addDeliveryRequestBy(ADDRESS_ID, TestDto.getOrderDeliveryDto(ORDER_ID + i));
        }

        // when
        List<OrderDeliveryDto> requestList = riderDao.getDeliveryRequestList(ADDRESS_ID);

        // then
        assertThat(requestList.size()).isEqualTo(30);
    }

    @Test
    void findOrderRequest_확인() {
        // given
        for (int i = 0; i < 10; i++) {
            riderDao.addDeliveryRequestBy(ADDRESS_ID, TestDto.getOrderDeliveryDto(ORDER_ID + i));
        }

        // when
        OrderDeliveryDto orderRequest = riderDao.findDeliveryRequest(ORDER_ID, ADDRESS_ID);

        // then
        assertThat(TestDto.getOrderDeliveryDto(ORDER_ID)).usingRecursiveComparison().isEqualTo(orderRequest);

    }

    @Test
    void findOrderRequest_존재하지_않는_요청_Null_반환() {
        // given
        for (int i = 0; i < 10; i++) {
            riderDao.addDeliveryRequestBy(ADDRESS_ID, TestDto.getOrderDeliveryDto(ORDER_ID + i));
        }

        // when
        OrderDeliveryDto orderRequest = riderDao.findDeliveryRequest(99L, ADDRESS_ID);

        // then
        assertThat(orderRequest).isNull();

    }

    @Test
    void acceptDelivery_확인() {
        // given
        for (int i = 0; i < 10; i++) {
            riderDao.addDeliveryRequestBy(ADDRESS_ID, TestDto.getOrderDeliveryDto(ORDER_ID + i));
        }
        // when
        riderDao.acceptDelivery(ADDRESS_ID, TestDto.getOrderDeliveryDto(ORDER_ID + 1L));

        // then
        assertThat(redisTemplate.opsForZSet().zCard(getKey("ORDER"))).isEqualTo(9);
    }


    @NotNull
    private String getKey(String key) {
        return key + ADDRESS_ID;
    }
}