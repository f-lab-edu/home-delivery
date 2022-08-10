package com.flab.delivery.service;

import com.flab.delivery.dto.category.CategoryDto;
import com.flab.delivery.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.flab.delivery.utils.CacheConstants.CATEGORY;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    @Cacheable(value = CATEGORY)
    public List<CategoryDto> getCategories() {
        return categoryMapper.findAllCategory();
    }

}
