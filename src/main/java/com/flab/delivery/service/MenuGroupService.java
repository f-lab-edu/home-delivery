package com.flab.delivery.service;

import com.flab.delivery.dto.menu.MenuGroupDto;
import com.flab.delivery.dto.menu.MenuGroupRequestDto;
import com.flab.delivery.exception.MenuGroupException;
import com.flab.delivery.mapper.MenuGroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuGroupService {

    private final MenuGroupMapper menuGroupMapper;

    public void createMenuGroup(MenuGroupRequestDto requestDto) {
        Optional<Long> existsMenuGroup = menuGroupMapper.existsByName(requestDto.getStoreId(), requestDto.getName());
        if (existsMenuGroup.isPresent()) {
            throw new MenuGroupException("이미 존재하는 메뉴 그룹입니다", HttpStatus.BAD_REQUEST);
        }
        menuGroupMapper.save(requestDto);
    }


    public void updateMenuGroup(Long id, MenuGroupRequestDto requestDto) {
        getMenuGroup(id);
        menuGroupMapper.updateById(id, requestDto);
    }

    public List<MenuGroupDto> getMenuGroupList(Long storeId) {
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


    // Question : mapper.xml에서 돌리는데 하나가 안되면 그건어떻게찾을가?
    public void updatePriority(List<MenuGroupDto> menuGroupDtoList) {
        menuGroupMapper.updatePriority(menuGroupDtoList);
    }
}
