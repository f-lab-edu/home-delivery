package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.annotation.SessionUserId;
import com.flab.delivery.dto.order.owner.OwnerOrderResponseDto;
import com.flab.delivery.dto.order.user.OrderSimpleResponseDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.response.CommonResult;
import com.flab.delivery.service.OwnerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OwnerOrderController {

    private final OwnerOrderService ownerOrderService;

    // TODO 테스트 케이스 작성

    @LoginCheck(userType = UserType.OWNER)
    @GetMapping("/owner/{storeId}")
    public CommonResult<List<OrderSimpleResponseDto>> getOwnerOrderList(@SessionUserId String userId,
                                                                         @PathVariable Long storeId) {
        List<OwnerOrderResponseDto> ownerOrderList = ownerOrderService.getOwnerOrderList(userId, storeId);
        return CommonResult.getDataSuccessResult(ownerOrderList);
    }


}
