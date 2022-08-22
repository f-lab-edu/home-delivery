package com.flab.delivery.service;

import com.flab.delivery.dto.menugroup.MenuGroupDto;
import com.flab.delivery.dto.menugroup.MenuGroupRequestDto;
import com.flab.delivery.exception.MenuGroupException;
import com.flab.delivery.mapper.MenuGroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuGroupService {

    private final MenuGroupMapper menuGroupMapper;

    public void createMenuGroup(MenuGroupRequestDto requestDto) {
        if (menuGroupMapper.existsByName(requestDto.getStoreId(), requestDto.getName()).isPresent()) {
            throw new MenuGroupException("이미 존재하는 메뉴 그룹입니다", HttpStatus.BAD_REQUEST);
        }
        menuGroupMapper.save(requestDto);
    }


    public void updateMenuGroup(Long id, MenuGroupRequestDto requestDto) {
        menuGroupMapper.updateById(id, requestDto);
    }

    public List<MenuGroupDto> getMenuGroupList(Long storeId) {
        return menuGroupMapper.findAllByStoreId(storeId);
    }

    public void deleteGroup(Long id) {
        menuGroupMapper.deleteById(id);
    }

    public void updatePriority(List<MenuGroupDto> menuGroupDtoList) {
        if (menuGroupMapper.findAllById(menuGroupDtoList).size() != menuGroupDtoList.size()) {
            throw new MenuGroupException("업데이트 중 존재하지 않는 id가 있습니다", HttpStatus.NOT_FOUND);
        }
        menuGroupMapper.updatePriority(menuGroupDtoList);
    }
}
