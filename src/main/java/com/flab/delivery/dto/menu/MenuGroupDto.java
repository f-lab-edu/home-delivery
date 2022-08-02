package com.flab.delivery.dto.menu;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuGroupDto {

    private Long id;
    private Long storeId;
    private String name;
    private String info;
    private Integer priority;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<MenuGroupDto> menuGroupDtoList;
}
