package com.flab.delivery.dto.order.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.dto.option.OptionDto;
import lombok.*;

import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class OrderMenuDto {

    @NotEmpty(message = "메뉴가 존재하지 않습니다.")
    private MenuDto menuDto;

    @Min(value = 1, message = "수량이 옳바르지 않습니다.")
    private Integer quantity;

    private List<OptionDto> optionList;
}
