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
public class OrderMenuHistoryDto {

    private String menuName;
    private int price;
    private int quantity;
    private List<OrderOptionHistoryDto> optionList;

    //TODO 같은 메뉴 다른 옵션 담으면 다른 메뉴로 처리
    public static List<OrderMenuHistoryDto> createHistory(List<OrderMenuDto> menuList) {

        HashMap<Long, OrderMenuHistoryDto> historyMap = new HashMap<>();

        createMenuHistory(menuList, historyMap);

        return new ArrayList<>(historyMap.values());
    }

    private static void createMenuHistory(List<OrderMenuDto> menuList, HashMap<Long, OrderMenuHistoryDto> historyMap) {
        for (OrderMenuDto orderMenuDto : menuList) {
            OrderMenuHistoryDto historyDto = from(orderMenuDto);
            createOptionHistory(orderMenuDto, historyDto);
            historyMap.put(orderMenuDto.getMenuDto().getId(), historyDto);
        }
    }

    private static void createOptionHistory(OrderMenuDto orderMenuDto, OrderMenuHistoryDto historyDto) {
        for (OptionDto optionDto : orderMenuDto.getOptionList()) {
            historyDto.getOptionList().add(OrderOptionHistoryDto.from(optionDto));
        }
    }


    private static OrderMenuHistoryDto from(OrderMenuDto orderMenuDto) {
        return OrderMenuHistoryDto.builder()
                .menuName(orderMenuDto.getMenuDto().getName())
                .price(orderMenuDto.getMenuDto().getPrice() * orderMenuDto.getQuantity())
                .quantity(orderMenuDto.getQuantity())
                .optionList(new ArrayList<>())
                .build();
    }


    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @Getter
    private static class OrderOptionHistoryDto {

        private String optionName;
        private int price;

        public static OrderOptionHistoryDto from(OptionDto optionDto) {
            return OrderOptionHistoryDto.builder()
                    .optionName(optionDto.getName())
                    .price(optionDto.getPrice())
                    .build();
        }
    }
}
