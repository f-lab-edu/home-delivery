package com.flab.delivery.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.dto.option.OptionDto;
import lombok.*;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class OrderMenuDto {

    private MenuDto menuDto;
    private int quantity;
    private List<OptionDto> optionList;

    public OrderMenuDto(MenuDto menuDto, List<OptionDto> optionList) {
        this.menuDto = menuDto;
        this.optionList = optionList;
    }
}
