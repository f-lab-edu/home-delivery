package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.annotation.SessionUserId;
import com.flab.delivery.dto.order.owner.OwnerOrderResponseDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.response.CommonResult;
import com.flab.delivery.service.OwnerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OwnerOrderController {

    private final OwnerOrderService ownerOrderService;


    @LoginCheck(userType = UserType.OWNER)
    @GetMapping("/owner/{storeId}")
    public CommonResult<List<OwnerOrderResponseDto>> getOwnerOrderList(@SessionUserId String userId,
                                                                         @PathVariable Long storeId) {
        List<OwnerOrderResponseDto> ownerOrderList = ownerOrderService.getOwnerOrderList(userId, storeId);
        return CommonResult.getDataSuccessResult(ownerOrderList);
    }


    @LoginCheck(userType = UserType.OWNER)
    @PatchMapping("/{orderId}/owner/approve")
    public CommonResult<Void> approveOrder(@SessionUserId String userId,
                                           @PathVariable Long orderId) {

        ownerOrderService.approveOrder(userId, orderId);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    @LoginCheck(userType = UserType.OWNER)
    @PatchMapping("/{orderId}/owner/cancel")
    public CommonResult<Void> cancelOrder(@SessionUserId String userId,
                                           @PathVariable Long orderId) {

        ownerOrderService.cancelOrder(userId, orderId);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

}
