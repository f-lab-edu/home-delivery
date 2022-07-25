package com.flab.delivery.service;

import com.flab.delivery.dto.store.StoreDto;
import com.flab.delivery.dto.store.StoreRequestDto;
import com.flab.delivery.enums.StoreStatus;
import com.flab.delivery.exception.NotFoundException;
import com.flab.delivery.mapper.StoreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreMapper storeMapper;

    @Override
    public void createStore(StoreRequestDto storeRequestDto, String userId) {
        // 매장 중복개설 매장중복 체크를 어떻게 할 것 인가
        // 매장 이름으로 중복 체크를 할것입니다 - 이름 + 상세주소
        Optional<Long> existsStore = storeMapper.existsByNameAndDetailAddress(storeRequestDto.getName(),
                storeRequestDto.getDetailAddress());
        if (existsStore.isPresent()) {
            throw new RuntimeException("이미 존재하는 매장입니다");
        }
        storeMapper.save(storeRequestDto, userId);
    }


    @Override
    public List<StoreDto> getOwnerStoreList(String userId) {
        return storeMapper.findAllByUserId(userId);
    }

    @Override
    public StoreDto getStore(Long storeId) {
        return findStore(storeId);
    }

    @Override
    public void updateStore(Long storeId, StoreRequestDto storeRequestDto) {
        findStore(storeId);
        storeMapper.updateById(storeId, storeRequestDto);
    }

    @Override
    public void deleteStore(Long storeId) {
        findStore(storeId);
        storeMapper.deleteById(storeId);
    }

    @Override
    public void changeStatus(Long storeId, StoreStatus status) {
        findStore(storeId);
        storeMapper.updateStatusById(storeId, status);
    }

    private StoreDto findStore(Long storeId) {
        return storeMapper.findById(storeId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 매장입니다")
        );
    }


}
