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

import java.util.List;
import javax.validation.Valid;


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
    public CommonResult<StoreDto> getStore(@PathVariable("id") Long id) {
        return CommonResult.getDataSuccessResult(storeService.getStore(id));
    }

    @LoginCheck(userType = UserType.OWNER)
    @PatchMapping("/{id}")
    public CommonResult<Void> updateStore(@PathVariable("id") Long id, @RequestBody @Valid StoreRequestDto storeRequestDto) {
        storeService.updateStore(id, storeRequestDto);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    @LoginCheck(userType = UserType.OWNER)
    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteStore(@PathVariable("id") Long id) {
        storeService.deleteStore(id);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    @LoginCheck(userType = UserType.OWNER)
    @PatchMapping("/{id}/status")
    public CommonResult<Void> changeStatus(@PathVariable("id") Long id, @RequestBody StoreDto storeDto) {
        storeService.changeStatus(id, storeDto.getStatus());
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

}
