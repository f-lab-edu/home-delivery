package com.flab.delivery.service;

import com.flab.delivery.dao.RiderDao;
import com.flab.delivery.dto.order.rider.OrderDeliveryDto;
import com.flab.delivery.exception.OrderException;
import com.flab.delivery.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.flab.delivery.exception.message.ErrorMessageConstants.NOT_EXIST_ORDER_REQUEST_MESSAGE;
import static com.flab.delivery.exception.message.ErrorMessageConstants.NOT_STAND_BY_RIDER;

@Service
@RequiredArgsConstructor
public class RiderOrderService {

    private final RiderDao riderDao;
    private final OrderMapper orderMapper;

    public List<OrderDeliveryDto> getDeliveryRequests(String userId, Long addressId) {

        ensureRiderIsStandBy(userId, addressId);

        return riderDao.getDeliveryRequestList(addressId);
    }

    public void acceptDeliveryBy(Long orderId, String userId, Long addressId) {

        ensureRiderIsStandBy(userId, addressId);

        OrderDeliveryDto findDeliveryRequest = getDeliveryRequestBy(orderId, addressId);

        if (!riderDao.acceptDelivery(addressId, findDeliveryRequest)) {
            throw new OrderException(NOT_EXIST_ORDER_REQUEST_MESSAGE, HttpStatus.CONFLICT);
        }

        orderMapper.updateOrderForDelivery(orderId, userId);
    }

    public void finishDeliveryBy(Long orderId, String userId, Long addressId) {
        ensureRiderIsStandBy(userId, addressId);
        orderMapper.updateOrderForFinish(orderId, userId);
    }

    public List<OrderDeliveryDto> getInDeliveryList(String userId, Long addressId) {
        ensureRiderIsStandBy(userId, addressId);

        return orderMapper.findInDeliveryList(userId);
    }

    public List<OrderDeliveryDto> getFinishDeliveryList(String userId, Long addressId, Long startId) {
        ensureRiderIsStandBy(userId, addressId);

        List<Long> ids = orderMapper.findFinishDeliveryPageIds(userId, startId);

        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        return orderMapper.findFinishDeliveryList(userId, ids);
    }

    private OrderDeliveryDto getDeliveryRequestBy(Long orderId, Long addressId) {
        OrderDeliveryDto orderRequest = riderDao.findDeliveryRequest(orderId, addressId);

        if (orderRequest == null) {
            throw new OrderException(NOT_EXIST_ORDER_REQUEST_MESSAGE, HttpStatus.CONFLICT);
        }

        return orderRequest;
    }

    private void ensureRiderIsStandBy(String userId, Long addressId) {
        if (!riderDao.isStandByRider(userId, addressId)) {
            throw new OrderException(NOT_STAND_BY_RIDER, HttpStatus.NOT_FOUND);
        }
    }
}
