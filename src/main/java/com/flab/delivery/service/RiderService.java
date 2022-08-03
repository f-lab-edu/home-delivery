package com.flab.delivery.service;

import com.flab.delivery.dao.RiderDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiderService {

    private final RiderDao riderDao;

    public void registerStandByRider(String userId, Long addressId) {
        riderDao.registerStandByRider(userId, addressId);
    }
}
