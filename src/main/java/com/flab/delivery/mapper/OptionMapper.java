package com.flab.delivery.mapper;

import com.flab.delivery.dto.option.OptionDto;
import com.flab.delivery.dto.option.OptionRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface OptionMapper {

    Optional<Long> existsByName(@Param("menuId")Long menuId, @Param("name") String optionName);

    void save(@Param("option") OptionRequestDto optionRequestDto);

    void updateById(@Param("id") Long id, @Param("option") OptionRequestDto requestDto);

    void deleteById(Long id);

    List<OptionDto> findAllByMenuId(Long menuId);

    Optional<OptionDto> findById(Long id);
}
