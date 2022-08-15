package com.flab.delivery.dto.order;


import com.flab.delivery.dto.menu.MenuDto;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class OrderHistoryDto {

    private String menuName;
    private int price;
    private int quantity;
    private List<OptionHistoryDto> optionHistoryList;

    public static List<OrderHistoryDto> createHistory(List<MenuDto> menuList) {

        HashMap<Long, OrderHistoryDto> historyMap = new HashMap<>();

        for (MenuDto menuDto : menuList) {
            if (!historyMap.containsKey(menuDto.getId())) {
                historyMap.put(menuDto.getId(), createNewOrderHistoryDto(menuDto));
            } else {
                OrderHistoryDto historyDto = historyMap.get(menuDto.getId());
                historyDto.addPriceAndQuantity(menuDto);
                historyMap.put(menuDto.getId(), historyDto);
            }
        }

        //TODO 옵션 추가

        return new ArrayList<>(historyMap.values());
    }

    private void addPriceAndQuantity(MenuDto menuDto) {
        this.price += menuDto.getPrice();
        this.quantity += 1;
    }


    private static OrderHistoryDto createNewOrderHistoryDto(MenuDto menuDto) {
        return OrderHistoryDto.builder()
                .menuName(menuDto.getName())
                .price(menuDto.getPrice())
                .quantity(1).build();
    }


    private class OptionHistoryDto {

        private String optionName;
        private int price;
        private int quantity;
    }
}
