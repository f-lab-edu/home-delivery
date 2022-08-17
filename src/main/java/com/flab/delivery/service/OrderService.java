package com.flab.delivery.service;

import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.dto.order.OrderRequestDto;
import com.flab.delivery.dto.order.OrderSimpleResponseDto;
import com.flab.delivery.enums.OrderStatus;
import com.flab.delivery.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final PayService mockPayService;
    private final AddressService addressService;
    private final OrderMapper orderMapper;

    @Transactional
    public void createOrder(String userId, OrderRequestDto orderRequestDto) {

        // 주문 생성
        OrderDto orderDto = OrderDto.from(orderRequestDto, addressService.getDeliveryAddress(userId));
        orderMapper.save(userId, orderDto);

        Long orderId = orderDto.getId();

        // 걸제 진행
        mockPayService.pay(orderId, orderRequestDto.getPayType());

        // 결제 완료 후 주문 상태 변경
        orderMapper.changeStatus(orderId, OrderStatus.ORDER_REQUEST);

        // TODO 사장님 알람

        // TODO 메시지큐 OR 구현
    }
    public List<OrderSimpleResponseDto> getOrderList(String userId, int startId) {

        List<Long> pageIds = orderMapper.findPageIds(userId, startId);

        if (pageIds.isEmpty()) {
            return new ArrayList<>();
        }

        return orderMapper.findAllByPageIds(pageIds);
    }

}
