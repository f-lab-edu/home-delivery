package com.flab.delivery.dto.order;


import com.flab.delivery.dto.option.OptionDto;
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

    public static List<OrderHistoryDto> createHistory(List<OrderMenuDto> menuList) {

        HashMap<Long, OrderHistoryDto> historyMap = new HashMap<>();

        createMenuHistory(menuList, historyMap);

        return new ArrayList<>(historyMap.values());
    }

    private static void createMenuHistory(List<OrderMenuDto> menuList, HashMap<Long, OrderHistoryDto> historyMap) {
        for (OrderMenuDto orderMenuDto : menuList) {
            OrderHistoryDto historyDto = from(orderMenuDto);
            createOptionHistory(orderMenuDto, historyDto);
            historyMap.put(orderMenuDto.getMenuDto().getId(), historyDto);
        }
    }

    private static void createOptionHistory(OrderMenuDto orderMenuDto, OrderHistoryDto historyDto) {
        for (OptionDto optionDto : orderMenuDto.getOptionList()) {
            historyDto.getOptionHistoryList().add(OptionHistoryDto.from(optionDto));
        }
    }


    private static OrderHistoryDto from(OrderMenuDto orderMenuDto) {
        return OrderHistoryDto.builder()
                .menuName(orderMenuDto.getMenuDto().getName())
                .price(orderMenuDto.getMenuDto().getPrice() * orderMenuDto.getQuantity())
                .quantity(orderMenuDto.getQuantity())
                .optionHistoryList(new ArrayList<>())
                .build();
    }


    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @Getter
    private static class OptionHistoryDto {

        private String optionName;
        private int price;

        public static OptionHistoryDto from(OptionDto optionDto) {
            return OptionHistoryDto.builder()
                    .optionName(optionDto.getName())
                    .price(optionDto.getPrice())
                    .build();
        }
    }
}
