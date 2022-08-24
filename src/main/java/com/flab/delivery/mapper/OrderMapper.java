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

    List<Long> findPageIds(@Param("userId") String userId, @Param("startId") int startId);

    List<OrderSimpleResponseDto> findAllByPageIds(List<Long> ids);

    OrderDto findByOrderId(Long orderId);

    List<OwnerOrderResponseDto> findAllOwnerOrderLimit100(Long storeId);

    Optional<OrderDeliveryDto> findDeliveryInfo(@Param("ownerId") String ownerId, @Param("orderId") Long orderId, @Param("storeId") Long storeId);

    //TODO 테스트 작성
    void updateOrderForDelivery(@Param("orderId") Long orderId, @Param("riderId") String riderId);

    void updateOrderForFinish(@Param("orderId") Long orderId, @Param("riderId") String userId);

    List<Long> findFinishDeliveryPageIds(@Param("riderId") String userId, @Param("startId") Long startId);

    List<OrderDeliveryDto> findFinishDeliveryList(@Param("riderId") String riderId, @Param("ids") List<Long> ids);

    List<OrderDeliveryDto> findInDeliveryList(String userId);
}
