package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.annotation.SessionUserId;
import com.flab.delivery.dto.order.OrderRequestDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.response.CommonResult;
import com.flab.delivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @LoginCheck(userType = UserType.USER)
    @PostMapping
    public CommonResult<Void> createOrder(@RequestBody OrderRequestDto orderRequestDto,
                                          @SessionUserId String userId) {

        orderService.createOrder(userId, orderRequestDto);

        return CommonResult.getSimpleSuccessResult(HttpStatus.CREATED.value());
    }

}
