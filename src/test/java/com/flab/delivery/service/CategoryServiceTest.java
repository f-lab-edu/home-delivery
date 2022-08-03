package com.flab.delivery.service;

import com.flab.delivery.dto.category.CategoryDto;
import com.flab.delivery.mapper.CategoryMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;

    @Mock
    CategoryMapper categoryMapper;

    @Test
    void getCategories_성공() {
        // given
        ArrayList<CategoryDto> list = new ArrayList<>();
        list.add(makeCategory("치킨", 1L));
        list.add(makeCategory("족발", 2L));

        given(categoryMapper.findAllCategory()).willReturn(list);
        // when

        List<CategoryDto> categories = categoryService.getCategories();

        // then
        assertThat(categories).isEqualTo(list);
        verify(categoryMapper).findAllCategory();
    }

    private CategoryDto makeCategory(String name, long id) {
        return CategoryDto.builder().id(id).name(name).build();
    }
}