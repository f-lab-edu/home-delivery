package com.flab.delivery.dto.category;

import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class CategoryDto {

    private Long id;

    private String name;
}
