package com.flab.delivery.dto.order.user;

import com.flab.delivery.annotation.ValidEnum;
import com.flab.delivery.dto.option.OptionDto;
import com.flab.delivery.enums.PayType;
import lombok.*;

import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class OrderRequestDto {

    @Min(value = 1, message = "매장 정보가 올바르지 않습니다")
    @NotNull(message = "매장 정보가 올바르지 않습니다")
    private Long storeId;
    @NotEmpty(message = "주문 하실 메뉴가 없습니다.")
    private List<OrderMenuDto> menuList;


    @ValidEnum(enumClass = PayType.class, message = "결제 방법이 옳바르지 않습니다")
    private PayType payType;

    public int getTotalPrice() {

        int totalPrice = 0;
        for (OrderMenuDto menuDto : menuList) {
            int price = 0;
            price += menuDto.getMenuDto().getPrice();
            for (OptionDto optionDto : menuDto.getOptionList()) {
                price += optionDto.getPrice();
            }

            price *= menuDto.getQuantity();
            totalPrice += price;
        }

        return totalPrice;
    }

}
