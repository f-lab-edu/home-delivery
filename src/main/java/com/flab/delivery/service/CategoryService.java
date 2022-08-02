package com.flab.delivery.service;

import com.flab.delivery.dto.category.CategoryDto;
import com.flab.delivery.dto.store.StoreDto;
import com.flab.delivery.mapper.CategoryMapper;
import com.flab.delivery.mapper.StoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final StoreMapper storeMapper;

    public List<CategoryDto> getCategories() {
        return categoryMapper.findAllCategory();
    }

    public List<StoreDto> getStoreListBy(Long categoryId, Long addressId) {
        return storeMapper.findStoreListBy(categoryId, addressId);
    }
}
