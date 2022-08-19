package com.flab.delivery.service;

import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.dto.order.owner.OwnerOrderResponseDto;
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

import static com.flab.delivery.exception.message.ErrorMessageConstants.BAD_INPUT_MESSAGE;

@Service
@RequiredArgsConstructor
public class OwnerOrderService {

    private final OrderMapper orderMapper;

    private final StoreService storeService;

    public List<OwnerOrderResponseDto> getOwnerOrderList(String userId, Long storeId) {

        if (!storeService.existsStoreByUserIdAndStoreId(userId, storeId)) {
            return new ArrayList<>();
        }

        List<OwnerOrderResponseDto> orderList = orderMapper.findAllOwnerOrderLimit100(storeId);

        return orderList.stream()
                .filter(order -> order.getStatus() != OrderStatus.BEFORE_PAYMENT)
                .filter(order -> order.getCreatedAt().plusDays(3).isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    public void approveOrder(String userId, Long orderId) {
        OrderDto findOrder = orderMapper.findByIdAndUserId(orderId, userId);

        if (findOrder == null) {
            throw new OrderException(BAD_INPUT_MESSAGE, HttpStatus.BAD_REQUEST);
        }

        orderMapper.changeStatus(findOrder.getId(), OrderStatus.ORDER_APPROVED);
    }

    public void cancelOrder(String userId, Long orderId) {
        OrderDto findOrder = orderMapper.findByIdAndUserId(orderId, userId);

        if (findOrder == null) {
            throw new OrderException(BAD_INPUT_MESSAGE, HttpStatus.BAD_REQUEST);
        }

        orderMapper.changeStatus(findOrder.getId(), OrderStatus.ORDER_CANCELED);
    }
}
