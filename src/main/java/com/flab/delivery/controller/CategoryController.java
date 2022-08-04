package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.dto.category.CategoryDto;
import com.flab.delivery.dto.store.StoreDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.response.CommonResult;
import com.flab.delivery.service.CategoryService;
import com.flab.delivery.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final StoreService storeService;

    @LoginCheck(userType = UserType.USER)
    @GetMapping
    public CommonResult<List<CategoryDto>> getCategories() {
        return CommonResult.getDataSuccessResult(categoryService.getCategories());
    }

    @LoginCheck(userType = UserType.USER)
    @GetMapping("/{id}")
    public CommonResult<List<StoreDto>> getStoreListBy(@PathVariable Long id, @RequestParam Long addressId) {
        return CommonResult.getDataSuccessResult(storeService.getStoreListBy(id, addressId));
    }


}
