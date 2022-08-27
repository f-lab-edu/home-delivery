package com.flab.delivery.service;

import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.dto.order.user.OrderRequestDto;
import com.flab.delivery.dto.order.user.OrderSimpleResponseDto;
import com.flab.delivery.enums.OrderStatus;
import com.flab.delivery.mapper.OrderMapper;
import com.flab.delivery.utils.FCMAlarmConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.flab.delivery.utils.FCMAlarmConstants.*;


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

        // TODO 사장님 알람 ( 추가완료)
        fcmService.sendMessage(userId, REQUEST_COMPLETE_TITLE, REQUEST_COMPLETE);

        // TODO 메시지큐 OR 구현
    }

    public List<OrderSimpleResponseDto> getUserOrderList(String userId, int startId) {

        List<Long> pageIds = orderMapper.findPageIds(userId, startId);

        if (pageIds.isEmpty()) {
            return new ArrayList<>();
        }

        return orderMapper.findAllByPageIds(pageIds);
    }

    public OrderDto getUserDetailOrder(String userId, Long orderId) {
        return orderMapper.findByOrderId(orderId);
    }
}
