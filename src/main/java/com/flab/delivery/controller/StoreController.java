package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.annotation.SessionUserId;
import com.flab.delivery.dto.store.StoreDto;
import com.flab.delivery.dto.store.StoreRequestDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.response.CommonResult;
import com.flab.delivery.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;


    @LoginCheck(userType = UserType.OWNER)
    @PostMapping
    public CommonResult<Void> createStore(@RequestBody @Valid StoreRequestDto storeRequestDto,
                                          @SessionUserId String userId) {
        storeService.createStore(storeRequestDto, userId);
        return CommonResult.getSimpleSuccessResult(HttpStatus.CREATED.value());
    }

    @LoginCheck(userType = UserType.OWNER)
    @GetMapping
    public CommonResult<List<StoreDto>> getOwnerStoreList(@SessionUserId String userId) {
        List<StoreDto> ownerStoreList = storeService.getOwnerStoreList(userId);
        return CommonResult.getDataSuccessResult(ownerStoreList);
    }

    @LoginCheck(userType = UserType.OWNER)
    @GetMapping("/{id}")
    public CommonResult<StoreDto> getStore(@PathVariable("id") Long storeId) {
        return CommonResult.getDataSuccessResult(storeService.getStore(storeId));
    }

    @LoginCheck(userType = UserType.OWNER)
    @PutMapping("/{id}")
    public CommonResult<Void> updateStore(@PathVariable("id") Long storeId, @RequestBody @Valid StoreRequestDto storeRequestDto) {
        storeService.updateStore(storeId, storeRequestDto);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    @LoginCheck(userType = UserType.OWNER)
    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteStore(@PathVariable("id") Long storeId) {
        storeService.deleteStore(storeId);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    // PutMapping vs PatchMapping !!!
    @LoginCheck(userType = UserType.OWNER)
    @PutMapping("/{id}/status")
    public CommonResult<Void> changeStatus(@PathVariable("id") Long storeId, @RequestBody StoreDto storeDto) {
        storeService.changeStatus(storeId, storeDto.getStatus());
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

}
