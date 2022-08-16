package com.flab.delivery.controller;

import com.flab.delivery.annotation.LoginCheck;
import com.flab.delivery.dto.option.OptionDto;
import com.flab.delivery.dto.option.OptionRequestDto;
import com.flab.delivery.enums.UserType;
import com.flab.delivery.response.CommonResult;
import com.flab.delivery.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/options")
public class OptionController {

    private final OptionService optionService;

    @LoginCheck(userType = UserType.OWNER)
    @PostMapping
    public CommonResult<Void> createOption(@RequestBody @Valid OptionRequestDto optionRequestDto) {
        optionService.createOption(optionRequestDto);
        return CommonResult.getSimpleSuccessResult(HttpStatus.CREATED.value());
    }

    @LoginCheck(userType = UserType.OWNER)
    @PatchMapping("/{id}")
    public CommonResult<Void> updateOption(@PathVariable("id") Long id,
                                           @RequestBody @Valid OptionRequestDto optionRequestDto) {
        optionService.updateOption(id, optionRequestDto);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    @LoginCheck(userType = UserType.OWNER)
    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteOption(@PathVariable("id") Long id) {
        optionService.deleteOption(id);
        return CommonResult.getSimpleSuccessResult(HttpStatus.OK.value());
    }

    @LoginCheck
    @GetMapping
    public CommonResult<List<OptionDto>> getOptionList(@RequestParam(name = "menuId") Long menuId) {
        return CommonResult.getDataSuccessResult(optionService.getOptionList(menuId));
    }


}
