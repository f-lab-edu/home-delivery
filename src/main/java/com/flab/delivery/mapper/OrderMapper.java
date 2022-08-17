package com.flab.delivery.mapper;

import com.flab.delivery.dto.order.OrderDetailResponseDto;
import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.dto.order.OrderSimpleResponseDto;
import com.flab.delivery.enums.OrderStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface OrderMapper {

    void save(@Param("userId") String userId, @Param("order") OrderDto orderDto);

    Long changeStatus(@Param("orderId") Long orderId, @Param("orderStatus") OrderStatus orderRequest);

    List<Long> findPageIds(@Param("userId") String userId, @Param("startId") int startId);

    List<OrderSimpleResponseDto> findAllByPageIds(List<Long> ids);

    OrderDetailResponseDto findByIdAndUserId(@Param("id") Long orderId, @Param("userId") String userId);
}
