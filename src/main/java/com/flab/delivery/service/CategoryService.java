package com.flab.delivery.service;

import com.flab.delivery.dto.category.CategoryDto;
import com.flab.delivery.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getCategories() {
        return categoryMapper.findAllCategory();
    }
}
