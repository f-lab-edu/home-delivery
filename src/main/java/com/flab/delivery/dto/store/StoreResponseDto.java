package com.flab.delivery.dto.store;

import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.dto.menugroup.MenuGroupDto;
import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreResponseDto {

    private StoreDto storeDto;
    private List<MenuGroupDto> menuGroupDtoList;
    private List<MenuDto> menuDtoList;
}
