package com.flab.delivery.dto.menugroup;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MenuGroupDto {

    private Long id;
    private Long storeId;
    private String name;
    private String info;
    private Integer priority;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
