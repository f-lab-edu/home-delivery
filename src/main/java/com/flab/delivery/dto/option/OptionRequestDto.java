package com.flab.delivery.dto.option;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OptionRequestDto {

    @Min(value = 0, message = "메뉴아이디 정보가 올바르지 않습니다")
    @NotNull(message = "메뉴아이디 정보가 올바르지 않습니다")
    private Long menuId;

    @NotBlank(message = "옵션이름 정보가 올바르지 않습니다")
    private String name;

    @Min(value = 0, message = "옵션가격 정보가 올바르지 않습니다")
    @NotNull(message = "옵션가격 정보가 올바르지 않습니다")
    private Integer price;
}
