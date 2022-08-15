package com.flab.delivery.service;

import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.dto.menu.MenuRequestDto;
import com.flab.delivery.enums.MenuStatus;
import com.flab.delivery.exception.MenuException;
import com.flab.delivery.mapper.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuMapper menuMapper;


    public void createMenu(MenuRequestDto menuRequestDto) {
        if (menuMapper.existsByName(menuRequestDto.getMenuGroupId(), menuRequestDto.getName()).isPresent()) {
            throw new MenuException("이미 존재하는 메뉴 입니다", HttpStatus.BAD_REQUEST);
        }

        menuMapper.save(menuRequestDto);
    }

    public MenuDto getMenu(Long id) {
        return menuMapper.findById(id).orElseThrow(
                () -> new MenuException("존재하지 않는 메뉴입니다", HttpStatus.NOT_FOUND)
        );
    }

    public void updateMenu(Long id, MenuRequestDto menuRequestDto) {
        menuMapper.updateById(id, menuRequestDto);
    }

    public void deleteMenu(Long id) {
        menuMapper.deleteById(id);
    }

    public void updateStatus(Long id, MenuStatus status) {
        menuMapper.updateStatus(id, status);
    }

    public void updatePriority(List<MenuDto> menuDtoList) {
        if (menuMapper.findAllById(menuDtoList).size() != menuDtoList.size()) {
            throw new MenuException("업데이트 중 존재하지 않는 id가 있습니다", HttpStatus.NOT_FOUND);
        }
        menuMapper.updatePriority(menuDtoList);
    }

    public List<MenuDto> getMenuList(Long storeId) {
        return menuMapper.findAllByStoreId(storeId);
    }
}
