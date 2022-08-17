package com.flab.delivery.dto.order;


import com.flab.delivery.dto.option.OptionDto;
import lombok.*;
import org.assertj.core.util.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class OrderMenuHistoryDto {

    private String menuName;
    private int price;
    private int quantity;
    private List<OrderOptionHistoryDto> optionList;

    public static List<OrderMenuHistoryDto> createHistory(List<OrderMenuDto> menuList) {

        HashMap<String, OrderMenuHistoryDto> historyMap = new HashMap<>();

        createMenuHistory(menuList, historyMap);

        return new ArrayList<>(historyMap.values());
    }

    private static void createMenuHistory(List<OrderMenuDto> menuList, HashMap<String, OrderMenuHistoryDto> historyMap) {
        for (OrderMenuDto orderMenuDto : menuList) {
            OrderMenuHistoryDto historyDto = from(orderMenuDto);
            createOptionHistory(orderMenuDto, historyDto);
            historyMap.put(getHistoryKey(orderMenuDto), historyDto);
        }
    }

    private static String getHistoryKey(OrderMenuDto orderMenuDto) {
        List<Long> optionIdList = orderMenuDto.getOptionList()
                .stream()
                .map(OptionDto::getId)
                .collect(Collectors.toList());
        return Strings.concat(
                        String.valueOf(orderMenuDto.getMenuDto().getId()),
                        Strings.concat(optionIdList));
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

    public int calculateTotalPrice() {
        int price = getPrice();

        for (OrderOptionHistoryDto optionHistoryDto : optionList) {
            price += optionHistoryDto.price;
        }
        return price * getQuantity();
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
