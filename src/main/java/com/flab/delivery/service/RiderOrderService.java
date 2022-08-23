package com.flab.delivery.service;

import com.flab.delivery.dao.RiderDao;
import com.flab.delivery.dto.order.rider.OrderDeliveryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RiderOrderService {

    private final RiderDao riderDao;

    public List<OrderDeliveryDto> getDeliveryRequests(String userId, Long addressId) {

        if (!riderDao.isStandByRider(userId, addressId)) {
            return new ArrayList<>();
        }

        return riderDao.getDeliveryRequestList(addressId);
    }
}
