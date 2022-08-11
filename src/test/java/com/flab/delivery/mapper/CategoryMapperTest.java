package com.flab.delivery.mapper;

import com.flab.delivery.config.DatabaseConfig;
import com.flab.delivery.dto.category.CategoryDto;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@Import({DatabaseConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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