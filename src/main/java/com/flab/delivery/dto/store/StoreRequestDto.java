package com.flab.delivery.dto.store;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreRequestDto {

    @Min(value = 1, message = "주소 정보가 올바르지 않습니다")
    @NotNull(message = "주소 정보가 올바르지 않습니다")
    private Long addressId;

    @Min(value = 1, message = "카테고리 정보가 올바르지 않습니다")
    @NotNull(message = "카테고리 정보가 올바르지 않습니다")
    private Long categoryId;

    @NotBlank(message = "상세 주소 정보가 올바르지 않습니다")
    private String detailAddress;

    @NotBlank(message = "매장 이름 정보가 올바르지 않습니다")
    private String name;

    @NotNull(message = "핸드폰 정보가 올바르지 않습니다")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "핸드폰 정보가 올바르지 않습니다")
    private String phoneNumber;

    @NotBlank(message = "매장 설명 정보가 올바르지 않습니다")
    private String info;

    @NotBlank(message = "오픈 시간 정보가 올바르지 않습니다")
    private String openTime;

    @NotBlank(message = "마감 시간 정보가 올바르지 않습니다")
    private String endTime;

    @Min(value = 0, message = "최소 주문 가격 정보가 올바르지 않습니다")
    @NotNull(message = "최소 주문 가격 정보가 올바르지 않습니다")
    private Long minPrice;
}
