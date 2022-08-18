package com.flab.delivery.controller.validator;

import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.dto.option.OptionDto;
import com.flab.delivery.dto.order.user.OrderMenuDto;
import com.flab.delivery.dto.order.user.OrderRequestDto;
import com.flab.delivery.dto.store.StoreDto;
import com.flab.delivery.enums.MenuStatus;
import com.flab.delivery.enums.StoreStatus;
import com.flab.delivery.mapper.MenuMapper;
import com.flab.delivery.mapper.OptionMapper;
import com.flab.delivery.mapper.StoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderValidator implements Validator {

    private final MenuMapper menuMapper;
    private final StoreMapper storeMapper;

    private final OptionMapper optionMapper;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(OrderRequestDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OrderRequestDto orderRequestDto = (OrderRequestDto) target;

        Optional<StoreDto> findOptionalStore = storeMapper.findById(orderRequestDto.getStoreId());

        if (!findOptionalStore.isPresent()) {
            errors.rejectValue("storeId", "worng.value", "잘못된 요청 입니다.");
        }
        StoreDto findStore = findOptionalStore.get();

        if (isStoreOpen(findStore)) {
            errors.rejectValue("storeId", "worng.value", "매장이 오픈 상태가 아닙니다.");
            return;
        }

        if (!validateMenu(orderRequestDto)) {
            errors.rejectValue("menuList", "worng.value", "잘못된 요청 입니다.");
            return;
        }

        if (findStore.getMinPrice() > orderRequestDto.getTotalPrice()) {
            errors.rejectValue("menuList", "worng.value", "최소 주문 금액을 확인 해주세요");
        }

    }

    private boolean isStoreOpen(StoreDto findStore) {
        return findStore.getStatus() != StoreStatus.OPEN;
    }

    private boolean validateMenu(OrderRequestDto orderRequestDto) {

        for (OrderMenuDto orderMenuDto : orderRequestDto.getMenuList()) {

            // 입력된 가격과 수량이 이상할 경우
            MenuDto menuDto = orderMenuDto.getMenuDto();

            if (menuDto.getPrice() == null || menuDto.getPrice() == 0) {
                return false;
            }

            Optional<MenuDto> findOptionalMenu = menuMapper.findById(menuDto.getId());

            // 입력된 메뉴 Id 에 따른 메뉴가 DB 에 업을경우
            if (!findOptionalMenu.isPresent()) {
                return false;
            }

            MenuDto findMenu = findOptionalMenu.get();

            // 메뉴가 현재 소진 상태일경우
            if (findMenu.getStatus() == MenuStatus.SOLDOUT) {
                return false;
            }

            // 입력된 가격과 메뉴 가격이 동일하지 않은 경우
            if (!Objects.equals(findMenu.getPrice(), menuDto.getPrice())) {
                return false;
            }

            // 옵션이 없을 경우
            if (orderMenuDto.getOptionList() == null) {
                return true;
            }

            if (!validateOption(orderMenuDto, findMenu)) {
                return false;
            }

        }
        return true;
    }

    private boolean validateOption(OrderMenuDto orderMenuDto, MenuDto findMenu) {
        for (OptionDto optionDto : orderMenuDto.getOptionList()) {

            // 입력된 옵션 메뉴 Id 와 메뉴 Id 가 동일하지 않을경우
            if (!Objects.equals(optionDto.getMenuId(), findMenu.getId())) {
                return false;
            }

            Optional<OptionDto> findOption = optionMapper.findById(optionDto.getId());

            // 입력된 옵션 Id 에 따른 옵션이 DB 에 존재하지 않을경우
            if (!findOption.isPresent()) {
                return false;
            }

            // 입력된 옵션 가격과 옵션 가격이 다를경우
            if (!Objects.equals(findOption.get().getPrice(), optionDto.getPrice())) {
                return false;
            }

        }
        return true;
    }
}
