package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.annotation.SessionUserId;
import com.flab.delivery.dto.order.rider.OrderDeliveryDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.response.CommonResult;
import com.flab.delivery.service.RiderOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class RiderOrderController {

    private final RiderOrderService riderOrderService;

    @LoginCheck(userType = UserType.RIDER)
    @GetMapping("/rider/request")
    public CommonResult<List<OrderDeliveryDto>> getDeliveryRequestList(@SessionUserId String userId,
                                                                       @RequestParam Long addressId) {
        return CommonResult.getDataSuccessResult(riderOrderService.getDeliveryRequests(userId, addressId));
    }

    @LoginCheck(userType = UserType.RIDER)
    @PatchMapping("{orderId}/rider/accept")
    public CommonResult<Void> acceptDelivery(@SessionUserId String userId,
                                             @RequestParam Long addressId,
                                             @PathVariable Long orderId) {

        riderOrderService.acceptDeliveryBy(orderId, userId, addressId);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    @LoginCheck(userType = UserType.RIDER)
    @PatchMapping("{orderId}/rider/finish")
    public CommonResult<Void> finishDelivery(@SessionUserId String userId,
                                             @RequestParam Long addressId,
                                             @PathVariable Long orderId) {

        riderOrderService.finishDeliveryBy(orderId, userId, addressId);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    @LoginCheck(userType = UserType.RIDER)
    @GetMapping("/rider/in-delivery")
    public CommonResult<List<OrderDeliveryDto>> getInDeliveryList(@SessionUserId String userId,
                                                                  @RequestParam Long addressId) {

        return CommonResult.getDataSuccessResult(riderOrderService.getInDeliveryList(userId, addressId));
    }

    @LoginCheck(userType = UserType.RIDER)
    @GetMapping("/rider/finish")
    public CommonResult<List<OrderDeliveryDto>> getFinishDeliveryList(@SessionUserId String userId,
                                                                      @RequestParam Long addressId,
                                                                      @RequestParam(required = false) Long startId) {

        return CommonResult.getDataSuccessResult(riderOrderService.getFinishDeliveryList(userId, addressId, startId));
    }
}
