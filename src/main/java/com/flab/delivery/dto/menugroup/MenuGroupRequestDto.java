package com.flab.delivery.dto.menugroup;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuGroupRequestDto {

    @Min(value = 1, message = "매장 정보가 올바르지 않습니다")
    @NotNull(message = "매장 정보가 올바르지 않습니다")
    private Long storeId;

    @NotBlank(message = "메뉴그룹 이름 정보가 올바르지 않습니다")
    private String name;

    @NotBlank(message = "메뉴그룹 설명 정보가 올바르지 않습니다")
    private String info;
}
