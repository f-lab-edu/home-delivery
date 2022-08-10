package com.flab.delivery.service;

import com.flab.delivery.dto.menu.MenuDto;

import com.flab.delivery.dto.menu.MenuListResponseDto;
import com.flab.delivery.dto.menu.MenuRequestDto;
import com.flab.delivery.enums.MenuStatus;
import com.flab.delivery.exception.MenuException;
import com.flab.delivery.mapper.MenuGroupMapper;
import com.flab.delivery.mapper.MenuMapper;
import com.flab.delivery.mapper.StoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuMapper menuMapper;
    private final StoreMapper storeMapper;

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
        getMenu(id);
        menuMapper.updateById(id, menuRequestDto);
    }

    public void deleteMenu(Long id) {
        getMenu(id);
        menuMapper.deleteById(id);
    }

    public void updateStatus(Long id, MenuStatus status) {
        getMenu(id);
        menuMapper.updateStatus(id, status);
    }

    public void updatePriority(List<MenuDto> menuDtoList) {
        if (menuMapper.findAllById(menuDtoList).size() != menuDtoList.size()) {
            throw new MenuException("업데이트 중 존재하지 않는 id가 있습니다", HttpStatus.NOT_FOUND);
        }
        menuMapper.updatePriority(menuDtoList);
    }


    /**
     * LinkedHashMap을 사용한 이유는 menuGroupId를 우선순위별로 정렬해서 받아오기때문에
     * map에 넣으면서 조회한 순서대로 담기위해서 사용했습니다.
     */
    public List<MenuListResponseDto> getMenuList(Long storeId) {
        storeMapper.findById(storeId).orElseThrow(
                () -> new MenuException("존재하지않는 매장입니다", HttpStatus.NOT_FOUND)
        );

        List<MenuDto> menuList = menuMapper.findAllByStoreId(storeId);

        Map<Long, MenuListResponseDto> map = new LinkedHashMap<>();
        for (int i = 0; i < menuList.size(); i++) {
            MenuDto menuDto = menuList.get(i);
            Long menuGroupId = menuDto.getMenuGroupId();
            if (!map.containsKey(menuGroupId)) {
                map.put(menuGroupId, new MenuListResponseDto(menuGroupId));
            }
            MenuListResponseDto menuListResponseDto = map.get(menuGroupId);
            menuListResponseDto.insertMenu(menuDto);
            map.put(menuGroupId, menuListResponseDto);
        }
        return new ArrayList<>(map.values());
    }
}
