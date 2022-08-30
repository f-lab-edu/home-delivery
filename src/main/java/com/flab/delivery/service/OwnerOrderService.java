package com.flab.delivery.service;

import com.flab.delivery.dao.RiderDao;
import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.dto.order.owner.OwnerOrderResponseDto;
import com.flab.delivery.dto.order.rider.OrderDeliveryDto;
import com.flab.delivery.enums.OrderStatus;
import com.flab.delivery.exception.OrderException;
import com.flab.delivery.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.flab.delivery.exception.message.ErrorMessageConstants.BAD_REQUEST_MESSAGE;

@Service
@RequiredArgsConstructor
public class OwnerOrderService {

    private final OrderMapper orderMapper;

    private final StoreService storeService;

    private final RiderDao riderDao;

    public List<OwnerOrderResponseDto> getOwnerOrderList(String userId, Long storeId) {

        if (!hasStoreBy(userId, storeId)) {
            return new ArrayList<>();
        }

        List<OwnerOrderResponseDto> orderList = orderMapper.findAllOwnerOrderLimit100(storeId);

        return orderList.stream()
                .filter(order -> order.getStatus() != OrderStatus.BEFORE_PAYMENT)
                .filter(order -> order.getCreatedAt().plusDays(3).isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    public void approveOrder(String userId, Long orderId) {
        changeOrderStatus(orderId, userId, OrderStatus.ORDER_APPROVED);
    }

    public void cancelOrder(String userId, Long orderId) {
        changeOrderStatus(orderId, userId, OrderStatus.ORDER_CANCELED);
    }

    private void changeOrderStatus(Long orderId, String userId, OrderStatus orderCanceled) {
        OrderDto findOrder = orderMapper.findByOrderId(orderId);

        if (findOrder == null) {
            throw new OrderException(BAD_REQUEST_MESSAGE, HttpStatus.BAD_REQUEST);
        }

        if (!hasStoreBy(userId, findOrder.getStoreId())) {
            throw new OrderException(BAD_REQUEST_MESSAGE, HttpStatus.NOT_ACCEPTABLE);
        }


        orderMapper.changeStatus(findOrder.getId(), orderCanceled);
    }

    private boolean hasStoreBy(String userId, Long storeId) {
        return storeService.existsStoreByUserIdAndStoreId(userId, storeId);
    }

    public void callRider(String userId, Long orderId, Long storeId) {


        OrderDeliveryDto deliveryInfo = orderMapper.findDeliveryInfo(userId, orderId, storeId)
                .orElseThrow(() -> new OrderException(BAD_REQUEST_MESSAGE, HttpStatus.BAD_REQUEST));


        riderDao.addDeliveryRequestBy(deliveryInfo.getAddressId(), deliveryInfo);

    }

}
