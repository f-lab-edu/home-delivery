package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.dto.menugroup.MenuGroupDto;
import com.flab.delivery.dto.menugroup.MenuGroupRequestDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.response.CommonResult;
import com.flab.delivery.service.MenuGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menugroups")
public class MenuGroupController {

    private final MenuGroupService menuGroupService;

    @LoginCheck(userType = UserType.OWNER)
    @PostMapping
    public CommonResult<Void> createMenuGroup(@RequestBody @Valid MenuGroupRequestDto requestDto) {
        menuGroupService.createMenuGroup(requestDto);
        return CommonResult.getSimpleSuccessResult(HttpStatus.CREATED.value());
    }

    @LoginCheck(userType = UserType.OWNER)
    @PatchMapping("/{id}")
    public CommonResult<Void> updateMenuGroup(@PathVariable("id") Long id, @RequestBody @Valid MenuGroupRequestDto requestDto) {
        menuGroupService.updateMenuGroup(id, requestDto);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    @LoginCheck(userType = UserType.OWNER)
    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteGroup(@PathVariable("id") Long id, @RequestParam(name = "storeId") Long storeId) {
        menuGroupService.deleteGroup(id, storeId);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    @LoginCheck(userType = UserType.OWNER)
    @PatchMapping("/priorities")
    public CommonResult<Void> updatePriority(@RequestBody List<MenuGroupDto> request) {
        menuGroupService.updatePriority(request);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }



    @LoginCheck
    @GetMapping
    public CommonResult<List<MenuGroupDto>> getMenuGroupList(@RequestParam(name = "storeId") Long storeId) {
        List<MenuGroupDto> menuGroupList = menuGroupService.getMenuGroupList(storeId);
        return CommonResult.getDataSuccessResult(menuGroupList);
    }


}
