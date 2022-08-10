package com.flab.delivery.dto.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuListResponseDto {
    private Long menuGroupId;
    private List<MenuDto> menuDtoList;

    public MenuListResponseDto(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
        this.menuDtoList = new ArrayList<>();
    }

    public void insertMenu(MenuDto menuDto) {
        this.menuDtoList.add(menuDto);
    }

}
