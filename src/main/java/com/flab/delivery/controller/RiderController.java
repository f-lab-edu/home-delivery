package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.annotation.SessionUserId;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.response.CommonResult;
import com.flab.delivery.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/riders")
public class RiderController {

    private final RiderService riderService;

    @LoginCheck(userType = UserType.RIDER)
    @PostMapping("/standby")
    public CommonResult<Void> registerStandByRider(@SessionUserId String userId, @RequestParam Long addressId) {

        riderService.registerStandByRider(userId, addressId);

        return CommonResult.getSimpleSuccessResult(HttpStatus.CREATED.value());
    }

    @LoginCheck(userType = UserType.RIDER)
    @DeleteMapping("/standby")
    public CommonResult<Void> deleteStandByRider(@SessionUserId String userId, @RequestParam Long addressId) {

        riderService.deleteStandByRider(userId, addressId);

        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }
}
