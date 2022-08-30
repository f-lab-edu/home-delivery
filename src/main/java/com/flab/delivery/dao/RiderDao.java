package com.flab.delivery.dao;

import com.flab.delivery.dto.order.rider.OrderDeliveryDto;
import com.flab.delivery.exception.OrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.flab.delivery.exception.message.ErrorMessageConstants.NOT_ENOUGH_DELIVERY_REQUEST_TIME_MESSAGE;

@Repository
@RequiredArgsConstructor
public class RiderDao {

    private static final int MIN_REQUEST_TIME = 60 * 1000;
    private static final int RIDER_STAND_BY_EXPIRED_SECONDS = 24 * 60 * 60;
    private static final int REQUEST_DELIVERY_EXPIRED_SECONDS = 15 * 60;
    private final RedisTemplate<String, Object> redisTemplate;

    public void registerStandByRider(String userId, Long addressId) {
        redisTemplate.opsForSet().add(getRidersKeyBy(addressId), userId);
        registerExpireBy(getRidersKeyBy(addressId), RIDER_STAND_BY_EXPIRED_SECONDS);
    }

    private void registerExpireBy(String key, long timeout) {
        if (redisTemplate.getExpire(key) == -1) {
            redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
    }

    public void deleteStandByRider(String userId, Long addressId) {
        redisTemplate.opsForSet().remove(getRidersKeyBy(addressId), userId);
    }

    private String getRidersKeyBy(Long addressId) {
        return "RIDER" + addressId;
    }

    public void addDeliveryRequestBy(Long addressId, OrderDeliveryDto orderDeliveryDto) {

        Double score = redisTemplate.opsForZSet().score(getOrderKey(addressId), orderDeliveryDto);

        if (canAddDeliveryRequest(score)) {
            throw new OrderException(NOT_ENOUGH_DELIVERY_REQUEST_TIME_MESSAGE, HttpStatus.BAD_REQUEST);
        }

        redisTemplate.opsForZSet().add(getOrderKey(addressId), orderDeliveryDto, System.currentTimeMillis());
        registerExpireBy(getOrderKey(addressId), REQUEST_DELIVERY_EXPIRED_SECONDS);
    }

    private boolean canAddDeliveryRequest(Double score) {
        if (score != null && score > System.currentTimeMillis() - MIN_REQUEST_TIME) {
            return true;
        }
        return false;
    }

    private String getOrderKey(Long addressId) {
        return "ORDER" + addressId;
    }


    public boolean isStandByRider(String userId, Long addressId) {
        return redisTemplate.opsForSet().isMember(getRidersKeyBy(addressId), userId);
    }

    public List<OrderDeliveryDto> getDeliveryRequestList(Long addressId) {

        Set<Object> requestOrders = redisTemplate.opsForZSet().reverseRange(getOrderKey(addressId), 0, 29);

        return requestOrders
                .stream().map(o -> (OrderDeliveryDto) o)
                .collect(Collectors.toList());
    }

    public boolean acceptDelivery(Long addressId, OrderDeliveryDto deliveryRequest) {

        Long removed = redisTemplate.opsForZSet().remove(getOrderKey(addressId), deliveryRequest);

        return removed == 1;
    }


    public OrderDeliveryDto findDeliveryRequest(Long orderId, Long addressId) {

        OrderDeliveryDto findDeliveryRequest = redisTemplate.execute((RedisCallback<OrderDeliveryDto>) redisConnection -> {

            ScanOptions options = ScanOptions.scanOptions().count(100).build();
            Cursor<RedisZSetCommands.Tuple> scans = redisConnection.zScan(getOrderKey(addressId).getBytes(), options);

            while (scans.hasNext()) {
                RedisZSetCommands.Tuple next = scans.next();
                OrderDeliveryDto deliveryRequest = getDeliveryRequest(next);

                if (deliveryRequest.getOrderId().equals(orderId)) {
                    return deliveryRequest;
                }
            }

            return null;
        });

        return findDeliveryRequest;
    }

    private OrderDeliveryDto getDeliveryRequest(RedisZSetCommands.Tuple next) {
        return (OrderDeliveryDto) redisTemplate.getValueSerializer().deserialize(next.getValue());
    }
}
