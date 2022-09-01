package com.flab.delivery.mapper;

import com.flab.delivery.annotation.IntegrationTest;
import com.flab.delivery.dto.category.CategoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class CategoryMapperTest {

    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    void findAllCategory_확인() {
        // given
        // when
        List<CategoryDto> categoryDtoList = categoryMapper.findAllCategory();

        // then
        assertThat(categoryDtoList).isNotEmpty();
        assertThat(categoryDtoList).extracting(CategoryDto::getId).isNotNull();
        assertThat(categoryDtoList).extracting(CategoryDto::getName).isNotNull();
    }

}