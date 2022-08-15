package com.flab.delivery.dto.option;


import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OptionDto {

    private Long id;
    private Long menuId;
    private String name;
    private Integer price;
}
