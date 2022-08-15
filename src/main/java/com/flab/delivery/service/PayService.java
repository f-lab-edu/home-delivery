package com.flab.delivery.service;

import com.flab.delivery.enums.PayType;

public interface PayService {
    void pay(Long orderId, PayType payType);
}
