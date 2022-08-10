package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.dto.menu.MenuListResponseDto;
import com.flab.delivery.dto.menu.MenuRequestDto;
import com.flab.delivery.dto.menugroup.MenuGroupRequestDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.response.CommonResult;
import com.flab.delivery.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/menus")
public class MenuController {

    private final MenuService menuService;

    @LoginCheck(userType = UserType.OWNER)
    @PostMapping
    public CommonResult<Void> createMenu(@RequestBody @Valid MenuRequestDto menuRequestDto) {
        menuService.createMenu(menuRequestDto);
        return CommonResult.getSimpleSuccessResult(HttpStatus.CREATED.value());
    }

    @LoginCheck(userType = UserType.OWNER)
    @GetMapping("/{id}")
    public CommonResult<MenuDto> getMenu(@PathVariable("id") Long id) {
        return CommonResult.getDataSuccessResult(menuService.getMenu(id));
    }


    @LoginCheck(userType = UserType.OWNER)
    @PatchMapping("/{id}")
    public CommonResult<Void> updateMenu(@PathVariable("id") Long id, @RequestBody @Valid MenuRequestDto menuRequestDto) {
        menuService.updateMenu(id, menuRequestDto);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }


    @LoginCheck(userType = UserType.OWNER)
    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteMenu(@PathVariable("id") Long id) {
        menuService.deleteMenu(id);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    // 메뉴 상태변경
    // 고민이 RequestDto로 받는게 맞을지 Dto로 받는게 맞을지
    @LoginCheck(userType = UserType.OWNER)
    @PatchMapping("/{id}/status")
    public CommonResult<Void> updateStatus(@PathVariable("id") Long id, @RequestBody MenuDto menuDto) {
        menuService.updateStatus(id, menuDto.getStatus());
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    // 메뉴 우선순위 변경
    @LoginCheck(userType = UserType.OWNER)
    @PatchMapping("/priorities")
    public CommonResult<Void> updatePriority(@RequestBody List<MenuDto> menuDtoList) {
        menuService.updatePriority(menuDtoList);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }


    @LoginCheck(userType = UserType.OWNER)
    @GetMapping
    public CommonResult<List<MenuListResponseDto>> getMenuList(@RequestParam(name = "storeid") Long storeId) {
        return CommonResult.getDataSuccessResult(menuService.getMenuList(storeId));
    }
}
