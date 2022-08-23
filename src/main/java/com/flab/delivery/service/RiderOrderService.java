package com.flab.delivery.service;

import com.flab.delivery.dao.RiderDao;
import com.flab.delivery.dto.order.rider.OrderDeliveryDto;
import com.flab.delivery.exception.OrderException;
import com.flab.delivery.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.flab.delivery.exception.message.ErrorMessageConstants.*;

@Service
@RequiredArgsConstructor
public class RiderOrderService {

    private final RiderDao riderDao;
    private final OrderMapper orderMapper;

    public List<OrderDeliveryDto> getDeliveryRequests(String userId, Long addressId) {

        if (!riderDao.isStandByRider(userId, addressId)) {
            throw new OrderException(NOT_STAND_BY_RIDER, HttpStatus.NOT_FOUND);
        }

        return riderDao.getDeliveryRequestList(addressId);
    }

    public void acceptDeliveryBy(Long orderId, String userId, Long addressId) {

        if (!riderDao.isStandByRider(userId, addressId)) {
            throw new OrderException(CANT_DELIVERY_IN_LOCATION_MESSAGE, HttpStatus.BAD_REQUEST);
        }

        if (!riderDao.acceptDelivery(orderId, addressId)) {
            throw new OrderException(NOT_EXIST_ORDER_REQUEST_MESSAGE, HttpStatus.CONFLICT);
        }

        orderMapper.updateOrderForDelivery(orderId, userId);
    }
}
