package com.flab.delivery.service;

import com.flab.delivery.dto.pay.PayDto;
import com.flab.delivery.enums.PayType;
import com.flab.delivery.mapper.PayMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MockPayService implements PayService {

    private final PayMapper payMapper;

    /**
     * 가상 결제로 호출시 결제 완료
     */
    @Override
    public void pay(Long orderId, PayType payType) {
        PayDto payDto = PayDto.completePay(orderId, payType);
        payMapper.save(payDto);
    }
}
