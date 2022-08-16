package com.flab.delivery.dto.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.flab.delivery.dto.option.OptionDto;
import com.flab.delivery.enums.MenuStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MenuDto {

    private Long id;
    private Long menuGroupId;
    private String name;
    private String info;
    private Integer price;
    private MenuStatus status;
    private Integer priority;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
