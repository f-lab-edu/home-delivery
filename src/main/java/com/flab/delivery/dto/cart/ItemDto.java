package com.flab.delivery.dto.cart;


import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.dto.option.OptionDto;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemDto {

    private String cartKey;
    private MenuDto menuDto;
    private List<OptionDto> optionList;
    private int quantity;

    public ItemDto(MenuDto menuDto, List<OptionDto> optionList) {
        this.menuDto = menuDto;
        this.optionList = optionList;
        this.quantity = 1;
        this.cartKey = createHashKey(menuDto, optionList);
    }

    private String createHashKey(MenuDto menuDto, List<OptionDto> optionList) {
        List<String> optionIdList = optionList.stream().map(option -> String.valueOf(option.getId())).collect(Collectors.toList());
        String str = String.join("_", optionIdList);
        return menuDto.getId() + "_" + str;
    }

    public void increaseQuantity() {
        this.quantity += 1;
    }

    public void decreaseQuantity() {
        if (this.quantity > 1) {
            this.quantity -= 1;
        }
    }


}
