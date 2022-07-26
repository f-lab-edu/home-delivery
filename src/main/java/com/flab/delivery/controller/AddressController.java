package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.annotation.SessionUserId;
import com.flab.delivery.dto.address.AddressDto;
import com.flab.delivery.dto.address.AddressRequestDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.response.CommonResult;
import com.flab.delivery.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    @LoginCheck(userType = UserType.USER)
    public CommonResult<List<AddressDto>> getAllAddress(@SessionUserId String userId) {

        List<AddressDto> getAddressDtoList = addressService.getAllAddress(userId);

        return CommonResult.getDataSuccessResult(getAddressDtoList);
    }

    @PostMapping
    @LoginCheck(userType = UserType.USER)
    public CommonResult<Void> addAddress(@RequestBody @Valid AddressRequestDto addressRequestDto,
                                         @SessionUserId String userId) {

        addressService.addAddress(addressRequestDto, userId);

        return CommonResult.getSimpleSuccessResult(HttpStatus.CREATED.value());
    }

    @DeleteMapping("/{id}")
    @LoginCheck(userType = UserType.USER)
    public CommonResult<Void> deleteAddress(@PathVariable Long id, @SessionUserId String userId) {

        addressService.removeAddress(id, userId);

        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    @PutMapping("/{id}")
    @LoginCheck(userType = UserType.USER)
    public CommonResult<Void> selectAddress(@PathVariable Long id, @SessionUserId String userId) {

        addressService.selectAddress(id, userId);

        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

}
