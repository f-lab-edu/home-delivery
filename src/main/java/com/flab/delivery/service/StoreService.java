package com.flab.delivery.service;

import com.flab.delivery.dto.store.StoreDto;
import com.flab.delivery.dto.store.StoreRequestDto;
import com.flab.delivery.dto.store.StoreResponseDto;
import com.flab.delivery.enums.StoreStatus;
import com.flab.delivery.exception.StoreException;
import com.flab.delivery.mapper.StoreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.flab.delivery.utils.CacheConstants.STORE_LIST;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreMapper storeMapper;
    private final MenuService menuService;
    private final MenuGroupService menuGroupService;

    @CacheEvict(value = STORE_LIST, key = "#storeRequestDto.addressId")
    public void createStore(StoreRequestDto storeRequestDto, String userId) {
        Optional<Long> existsStore = storeMapper.existsByNameAndDetailAddress(storeRequestDto.getName(),
                storeRequestDto.getDetailAddress());
        if (existsStore.isPresent()) {
            throw new StoreException("이미 존재하는 매장입니다", HttpStatus.BAD_REQUEST);
        }
        storeMapper.save(storeRequestDto, userId);
    }

    public List<StoreDto> getOwnerStoreList(String userId) {
        return storeMapper.findAllByUserId(userId);
    }

    public StoreDto getStore(Long id) {
        return storeMapper.findById(id).orElseThrow(
                () -> new StoreException("존재하지 않는 매장입니다", HttpStatus.NOT_FOUND)
        );
    }

    public void updateStore(Long id, StoreRequestDto storeRequestDto) {
        getStore(id);
        storeMapper.updateById(id, storeRequestDto);
    }

    public void deleteStore(Long id) {
        getStore(id);
        storeMapper.deleteById(id);
    }

    public void changeStatus(Long id, StoreStatus status) {
        getStore(id);
        storeMapper.updateStatusById(id, status);
    }


    @Cacheable(value = STORE_LIST, key = "#addressId")
    public List<StoreDto> getStoreListBy(Long categoryId, Long addressId) {
        return storeMapper.findStoreListBy(categoryId, addressId);
    }

    public StoreResponseDto getStoreAndMenuAndMenuGroup(Long id) {
        return StoreResponseDto.builder()
                .storeDto(getStore(id))
                .menuGroupDtoList(menuGroupService.getMenuGroupList(id))
                .menuDtoList(menuService.getMenuList(id))
                .build();
    }

    public boolean existsStoreByUserIdAndStoreId(String userId, Long storeId) {
        return storeMapper.existsByUserIdAndStoreId(userId, storeId);
    }
}