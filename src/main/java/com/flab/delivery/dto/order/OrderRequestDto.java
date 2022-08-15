package com.flab.delivery.dto.order;

import com.flab.delivery.annotation.ValidEnum;
import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.enums.PayType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Getter
public class OrderRequestDto {

    @Min(value = 1, message = "매장 정보가 올바르지 않습니다")
    @NotNull(message = "매장 정보가 올바르지 않습니다")
    private Long storeId;

    private int deliveryPrice;

    @NotBlank(message = "주문 하실 메뉴가 없습니다.")
    private List<MenuDto> menuList;

    @NotBlank(message = "결제 방법은 필수 입니다.")
    @ValidEnum(enumClass = PayType.class)
    private PayType payType;

    //TODO 옵션 추가

    public int getTotalPrice() {

        int totalPrice = 0;
        for (MenuDto menuDto : menuList) {
            totalPrice += menuDto.getPrice();
        }

        return totalPrice + deliveryPrice;
    }

}
