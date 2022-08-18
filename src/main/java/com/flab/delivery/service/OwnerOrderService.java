package com.flab.delivery.service;

import com.flab.delivery.dto.order.owner.OwnerOrderResponseDto;
import com.flab.delivery.enums.OrderStatus;
import com.flab.delivery.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnerOrderService {

    private final OrderMapper orderMapper;

    private final StoreService storeService;

    // TODO 테스트 케이스 작성
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
}
