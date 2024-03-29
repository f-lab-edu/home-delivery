package com.flab.delivery.service;

import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.dto.order.user.OrderRequestDto;
import com.flab.delivery.dto.order.user.OrderSimpleResponseDto;
import com.flab.delivery.enums.OrderStatus;
import com.flab.delivery.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.flab.delivery.utils.FCMAlarmConstants.REQUEST_COMPLETE;
import static com.flab.delivery.utils.FCMAlarmConstants.REQUEST_COMPLETE_TITLE;


@Service
@RequiredArgsConstructor
public class UserOrderService {

    private final PayService mockPayService;
    private final AddressService addressService;
    private final OrderMapper orderMapper;
    private final FCMService fcmService;

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

        fcmService.sendMessage(userId, REQUEST_COMPLETE_TITLE, REQUEST_COMPLETE);
    }

    public List<OrderSimpleResponseDto> getUserOrderList(String userId, Long startId) {
        return orderMapper.findAllByPageIds(userId, startId);
    }

    public OrderDto getUserDetailOrder(Long orderId) {
        return orderMapper.findByOrderId(orderId);
    }
}
