package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.annotation.SessionUserId;
import com.flab.delivery.dto.cart.CartResponseDto;
import com.flab.delivery.dto.cart.ItemDto;
import com.flab.delivery.enums.OperationType;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.response.CommonResult;
import com.flab.delivery.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @LoginCheck(userType = UserType.USER)
    @PostMapping
    public CommonResult<Void> insertItem(@RequestBody ItemDto itemDto, @SessionUserId String userId,
                                         @RequestParam("storeId") String storeId) {
        cartService.insertItem(userId, storeId, itemDto);
        return CommonResult.getSimpleSuccessResult(HttpStatus.CREATED.value());
    }

    @LoginCheck(userType = UserType.USER)
    @DeleteMapping("/{cartKey}")
    public CommonResult<Void> deleteItem(@SessionUserId String userId, @PathVariable("cartKey") String cartKey) {
        cartService.deleteItem(userId, cartKey);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    @LoginCheck(userType = UserType.USER)
    @GetMapping
    public CommonResult<CartResponseDto> getItemList(@SessionUserId String userId) {
        return CommonResult.getDataSuccessResult(cartService.findAllItem(userId));
    }


    @LoginCheck(userType = UserType.USER)
    @PatchMapping("/{cartKey}")
    public CommonResult<Void> updateQuantity(@SessionUserId String userId, @PathVariable("cartKey") String cartKey,
                                             @RequestParam("operation") OperationType operationType) {
        switch (operationType) {
            case PLUS:
                cartService.increaseQuantity(userId, cartKey);
                break;
            case MINUS:
                cartService.decreaseQuantity(userId, cartKey);
                break;
        }
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }


}
