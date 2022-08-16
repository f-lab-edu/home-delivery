package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.annotation.SessionUserId;
import com.flab.delivery.controller.validator.OrderValidator;
import com.flab.delivery.dto.order.OrderRequestDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.response.CommonResult;
import com.flab.delivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderValidator orderValidator;


    @InitBinder("orderRequestDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(orderValidator);
    }

    @LoginCheck(userType = UserType.USER)
    @PostMapping
    public CommonResult<Void> createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto,
                                          @SessionUserId String userId) {

        orderService.createOrder(userId, orderRequestDto);

        return CommonResult.getSimpleSuccessResult(HttpStatus.CREATED.value());
    }

}
