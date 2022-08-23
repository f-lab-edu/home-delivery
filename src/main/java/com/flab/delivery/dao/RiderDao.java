package com.flab.delivery.dao;

import com.flab.delivery.dto.order.rider.OrderDeliveryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RiderDao {

    private static final int MIN_REQUEST_TIME = 60 * 1000;
    private final RedisTemplate<String, Object> redisTemplate;


    public void registerStandByRider(String userId, Long addressId) {
        redisTemplate.opsForSet().add(getRidersKeyBy(addressId), userId);
        redisTemplate.expire(getRidersKeyBy(addressId), 1, TimeUnit.DAYS);
    }

    public void deleteStandByRider(String userId, Long addressId) {
        redisTemplate.opsForSet().remove(getRidersKeyBy(addressId), userId);
    }

    private String getRidersKeyBy(Long addressId) {
        return "RIDER" + addressId;
    }

    public boolean addOrderBy(Long addressId, OrderDeliveryDto orderDeliveryDto) {

        Double score = redisTemplate.opsForZSet().score(getOrderKey(addressId), orderDeliveryDto);
        if (score != null && score > System.currentTimeMillis() - MIN_REQUEST_TIME) {
            return false;
        }

        redisTemplate.opsForZSet().add(getOrderKey(addressId), orderDeliveryDto, System.currentTimeMillis());
        return true;
    }

    private String getOrderKey(Long addressId) {
        return "ORDER" + addressId;
    }


    public boolean isStandByRider(String userId, Long addressId) {
        return redisTemplate.opsForSet().isMember(getRidersKeyBy(addressId), userId);
    }

    public List<OrderDeliveryDto> getDeliveryRequestList(Long addressId) {

        Set<Object> requestOrders = redisTemplate.opsForZSet().reverseRange(getOrderKey(addressId), 0, 30);

        return requestOrders
                .stream().map(o -> (OrderDeliveryDto) o)
                .collect(Collectors.toList());
    }
}
