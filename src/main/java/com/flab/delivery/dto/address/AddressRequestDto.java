package com.flab.delivery.dto.address;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class AddressRequestDto {

    @NotBlank(message = "주소는 필수 입력값입니다")
    private String townName;

    @NotBlank(message = "상세 주소는 필수 입력값입니다")
    private String detailAddress;

    private String alias;

    public void changeAlias(String alias) {
        this.alias = alias;
    }
}
