package com.flab.delivery.service;

import com.flab.delivery.dto.menugroup.MenuGroupDto;
import com.flab.delivery.dto.menugroup.MenuGroupRequestDto;
import com.flab.delivery.exception.MenuGroupException;
import com.flab.delivery.mapper.MenuGroupMapper;
import com.flab.delivery.mapper.StoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuGroupService {

    private final MenuGroupMapper menuGroupMapper;
    private final StoreMapper storeMapper;

    public void createMenuGroup(MenuGroupRequestDto requestDto) {
        if (menuGroupMapper.existsByName(requestDto.getStoreId(), requestDto.getName()).isPresent()) {
            throw new MenuGroupException("이미 존재하는 메뉴 그룹입니다", HttpStatus.BAD_REQUEST);
        }
        menuGroupMapper.save(requestDto);
    }


    public void updateMenuGroup(Long id, MenuGroupRequestDto requestDto) {
        getMenuGroup(id);
        menuGroupMapper.updateById(id, requestDto);
    }

    public List<MenuGroupDto> getMenuGroupList(Long storeId) {
        storeMapper.findById(storeId).orElseThrow(
                () -> new MenuGroupException("존재하지 않는 매장입니다", HttpStatus.NOT_FOUND)
        );
        return menuGroupMapper.findAllByStoreId(storeId);
    }

    public void deleteGroup(Long id) {
        getMenuGroup(id);
        menuGroupMapper.deleteById(id);
    }

    public MenuGroupDto getMenuGroup(Long id) {
        return menuGroupMapper.findById(id).orElseThrow(
                () -> new MenuGroupException("존재하지 않는 메뉴 그룹입니다", HttpStatus.NOT_FOUND)
        );
    }

    public void updatePriority(List<MenuGroupDto> menuGroupDtoList) {
        if (menuGroupMapper.findAllById(menuGroupDtoList).size() != menuGroupDtoList.size()) {
            throw new MenuGroupException("업데이트 중 존재하지 않는 id가 있습니다", HttpStatus.NOT_FOUND);
        }
        menuGroupMapper.updatePriority(menuGroupDtoList);
    }
}
