package com.flab.delivery.mapper;

import com.flab.delivery.dto.category.CategoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<CategoryDto> findAllCategory();
}
