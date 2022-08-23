package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.annotation.SessionUserId;
import com.flab.delivery.dto.order.rider.OrderDeliveryDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.response.CommonResult;
import com.flab.delivery.service.RiderOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class RiderOrderController {

    private final RiderOrderService riderOrderService;


    @LoginCheck(userType = UserType.RIDER)
    @GetMapping("/rider/request")
    public CommonResult<List<OrderDeliveryDto>> getDeliveryList(@SessionUserId String userId,
                                                                @RequestParam Long addressId) {
        return CommonResult.getDataSuccessResult(riderOrderService.getDeliveryRequests(userId, addressId));
    }
}
