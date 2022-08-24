package com.flab.delivery.dao;

import com.flab.delivery.dto.order.rider.OrderDeliveryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
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

    public boolean acceptDelivery(Long addressId, OrderDeliveryDto deliveryRequest) {

        List<Object> execute = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {

                operations.watch(getOrderKey(addressId));
                operations.multi();
                operations.opsForZSet().remove(getOrderKey(addressId), deliveryRequest);
                return operations.exec();
            }
        });

        return isRemovedDeliveryRequest(execute);
    }

    private boolean isRemovedDeliveryRequest(List<Object> execute) {
        return !execute.isEmpty() && Objects.equals(execute.get(0).toString(), "1");
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
