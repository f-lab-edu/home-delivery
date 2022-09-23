package com.flab.delivery.mapper;

import com.flab.delivery.dto.order.OrderDto;
import com.flab.delivery.dto.order.owner.OwnerOrderResponseDto;
import com.flab.delivery.dto.order.rider.OrderDeliveryDto;
import com.flab.delivery.dto.order.user.OrderSimpleResponseDto;
import com.flab.delivery.enums.OrderStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface OrderMapper {

    void save(@Param("userId") String userId, @Param("order") OrderDto orderDto);

    Long changeStatus(@Param("orderId") Long orderId, @Param("orderStatus") OrderStatus orderRequest);

    List<OrderSimpleResponseDto> findAllByPageIds(@Param("userId") String userId, @Param("startId") Long startId);

    OrderDto findByOrderId(Long orderId);

    List<OwnerOrderResponseDto> findAllOwnerOrderLimit100(Long storeId);

    Optional<OrderDeliveryDto> findDeliveryInfo(@Param("ownerId") String ownerId, @Param("orderId") Long orderId, @Param("storeId") Long storeId);

    Long updateOrderForDelivery(@Param("orderId") Long orderId, @Param("riderId") String riderId);

    Long updateOrderForFinish(@Param("orderId") Long orderId, @Param("riderId") String userId);

    List<OrderDeliveryDto> findFinishDeliveryList(@Param("riderId") String riderId, @Param("startId") Long startId);

    List<OrderDeliveryDto> findInDeliveryList(String userId);
}
