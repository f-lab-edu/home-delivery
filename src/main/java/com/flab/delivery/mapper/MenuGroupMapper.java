package com.flab.delivery.mapper;

import com.flab.delivery.dto.menu.MenuGroupDto;
import com.flab.delivery.dto.menu.MenuGroupRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MenuGroupMapper {

    void save(@Param("menuGroup")MenuGroupRequestDto menuGroupRequestDto);

    Optional<Long> existsByName(@Param("storeId") Long storeId, @Param("name") String name);

    void updateById(@Param("id") Long menuGroupId, @Param("menuGroup") MenuGroupRequestDto menuGroupRequestDto);

    Optional<MenuGroupDto> findById(@Param("id") Long menuGroupId);

    List<MenuGroupDto> findAllByStoreId(Long storeId);

    void deleteById(@Param("id") Long menuGroupId);

    void updatePriority(@Param("groupList") List<MenuGroupDto> menuGroupDtoList);

    List<MenuGroupDto> findAllById(@Param("groupList") List<MenuGroupDto> menuGroupDtoList);
}
