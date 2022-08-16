package com.flab.delivery.dto.option;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class OptionDto {

    private Long id;
    private Long menuId;
    private String name;
    private Integer price;
}
