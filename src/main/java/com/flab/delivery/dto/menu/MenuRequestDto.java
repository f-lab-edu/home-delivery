package com.flab.delivery.dto.menu;


import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuRequestDto {

    @Min(value = 1, message = "매뉴 그룹 정보가 올바르지 않습니다")
    @NotNull(message = "매뉴 그룹 정보가 올바르지 않습니다")
    private Long menuGroupId;

    @NotBlank(message = "메뉴 이름 정보가 올바르지 않습니다")
    private String name;
    @NotBlank(message = "메뉴 설명 정보가 올바르지 않습니다")
    private String info;

    @Min(value = 0, message = "메뉴 가격 정보가 올바르지 않습니다")
    @NotNull(message = "메뉴 가격 정보가 올바르지 않습니다")
    private Integer price;
}
