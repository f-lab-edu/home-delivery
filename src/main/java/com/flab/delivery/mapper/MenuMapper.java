package com.flab.delivery.mapper;

import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.dto.menu.MenuRequestDto;
import com.flab.delivery.enums.MenuStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Mapper
public interface MenuMapper {

    Optional<Long> existsByName(@Param("groupId") Long groupId, @Param("name") String name);

    void save(@Param("menu") MenuRequestDto menuRequestDto);

    Optional<MenuDto> findById(Long id);

    void updateById(@Param("id") Long id, @Param("menu") MenuRequestDto menuRequestDto);

    void deleteById(Long id);
    
    void updateStatus(@Param("id") Long id, @Param("status") MenuStatus menuStatus);

    List<MenuDto> findAllByStoreId(Long storeId);

    List<MenuDto> findAllById(@Param("menuList") List<MenuDto> menuDtoList);

    void updatePriority(@Param("menuList") List<MenuDto> menuDtoList);
}
