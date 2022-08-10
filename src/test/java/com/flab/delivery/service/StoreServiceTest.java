package com.flab.delivery.service;

import com.flab.delivery.dto.store.StoreDto;
import com.flab.delivery.dto.store.StoreRequestDto;
import com.flab.delivery.enums.StoreStatus;
import com.flab.delivery.exception.StoreException;
import com.flab.delivery.mapper.StoreMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @InjectMocks
    StoreService storeService;

    @Mock
    StoreMapper storeMapper;

    @Nested
    @DisplayName("매장 생성")
    class CreateStore {

        private StoreRequestDto requestDto;

        @BeforeEach
        void setUp() {
            requestDto = StoreRequestDto.builder()
                    .addressId(1L)
                    .categoryId(1L)
                    .detailAddress("북구 서하로 96")
                    .name("BBQ치킨")
                    .info("황금올리브 팝니다")
                    .phoneNumber("010-1111-1111")
                    .openTime("오전:12시")
                    .endTime("오후 10시")
                    .minPrice(18000L)
                    .build();
        }

        @Nested
        @DisplayName("성공")
        class SuccessCase {

            @DisplayName("성공하는 경우")
            @Test
            void success() {
                // given
                when(storeMapper.existsByNameAndDetailAddress(requestDto.getName(), requestDto.getDetailAddress()))
                        .thenReturn(Optional.empty());
                // when
                storeService.createStore(requestDto, "user1");

                when(storeMapper.existsByNameAndDetailAddress(requestDto.getName(), requestDto.getDetailAddress()))
                        .thenReturn(Optional.of(1L));
                Optional<Long> existsStore = storeMapper.existsByNameAndDetailAddress(requestDto.getName(), requestDto.getDetailAddress());
                // then
                Assertions.assertNotNull(existsStore.get());
            }
        }

        @Nested
        @DisplayName("실패")
        class FailCase {
            @DisplayName("이미 존재하는 경우")
            @Test
            void fail() {
                // given
                when(storeMapper.existsByNameAndDetailAddress(requestDto.getName(), requestDto.getDetailAddress()))
                        .thenReturn(Optional.of(1L));
                // when
                StoreException ex = Assertions.assertThrows(StoreException.class, () -> {
                    storeService.createStore(requestDto, "user1");
                });
                // then
                Assertions.assertEquals(ex.getMessage(), "이미 존재하는 매장입니다");
            }

        }
    }

    @Nested
    @DisplayName("소유 매장 조회")
    class getOwnerStoreList {

        @Nested
        @DisplayName("성공")
        class SuccessCase {
            @Test
            @DisplayName("조회 성공")
            void getOwnerStoreList_Success(@Mock List<StoreDto> storeList) {
                // given
                when(storeList.size()).thenReturn(1);
                when(storeMapper.findAllByUserId("user1")).thenReturn(storeList);
                // when
                List<StoreDto> list = storeService.getOwnerStoreList("user1");
                // then
                Assertions.assertEquals(1, list.size());
            }
        }
    }

    @Nested
    @DisplayName("매장 상세 조회")
    class getStore {
        private final Long storeId = 1L;
        private StoreDto storeDto;

        @BeforeEach
        void setUp() {
            storeDto = StoreDto.builder()
                    .id(storeId)
                    .addressId(1L)
                    .categoryId(1L)
                    .detailAddress("북구 서하로 96")
                    .name("BBQ치킨")
                    .info("황금올리브 팝니다")
                    .phoneNumber("010-1111-1111")
                    .status(StoreStatus.OPEN)
                    .openTime("오전:12시")
                    .endTime("오후 10시")
                    .minPrice(18000L)
                    .build();
        }

        @Nested
        @DisplayName("성공")
        class SuccessCase {
            @DisplayName("상세조회 성공")
            @Test
            void getStore_Success() {
                // given
                when(storeMapper.findById(storeId)).thenReturn(Optional.of(storeDto));
                // when
                StoreDto getStore = storeService.getStore(storeId);
                // then
                Assertions.assertEquals(getStore.getId(), storeId);
            }
        }

        @Nested
        @DisplayName("실패")
        class FailCase {
            @DisplayName("존재하지 않는경우")
            @Test
            void getStore_Fail_NotFound() {
                // given
                when(storeMapper.findById(storeId)).thenReturn(Optional.empty());
                // when
                StoreException ex = Assertions.assertThrows(StoreException.class, () -> {
                    storeService.getStore(storeId);
                });
                Assertions.assertEquals(ex.getMessage(), "존재하지 않는 매장입니다");
            }

        }
    }


    @Nested
    @DisplayName("매장 정보 업데이트")
    class updateStore {
        private final Long storeId = 1L;
        private StoreRequestDto storeRequestDto;

        @BeforeEach
        void setUp() {
            storeRequestDto = StoreRequestDto.builder()
                    .addressId(1L)
                    .categoryId(1L)
                    .detailAddress("북구 서하로 96")
                    .name("BBQ치킨")
                    .info("황금올리브 팝니다")
                    .phoneNumber("010-1111-1111")
                    .openTime("오전:12시")
                    .endTime("오후 10시")
                    .minPrice(20000L)
                    .build();
        }

        @Nested
        @DisplayName("성공")
        class SuccessCase {

            @Test
            @DisplayName("업데이트 성공")
            void updateStore_Success() {
                // given
                long before = 18000L;
                when(storeMapper.findById(storeId)).thenReturn(Optional.of(StoreDto.builder()
                        .minPrice(before)
                        .build()));
                // when
                storeMapper.updateById(storeId, storeRequestDto);
                when(storeMapper.findById(storeId)).thenReturn(Optional.of(StoreDto.builder()
                        .minPrice(storeRequestDto.getMinPrice())
                        .build()));
                Optional<StoreDto> after = storeMapper.findById(storeId);
                // then
                Assertions.assertNotEquals(before, after.get().getMinPrice());
            }
        }

        @Nested
        @DisplayName("실패")
        class FailCase {
            @Test
            @DisplayName("존재하지 않는 경우")
            void updateStore_Fail() {
                // given
                when(storeMapper.findById(storeId)).thenReturn(Optional.empty());
                // when
                StoreException ex = Assertions.assertThrows(StoreException.class, () -> {
                    storeService.updateStore(storeId, storeRequestDto);
                });
                // then
                Assertions.assertEquals(ex.getMessage(), "존재하지 않는 매장입니다");
            }
        }
    }

    @Nested
    @DisplayName("매장 삭제")
    class deleteStore {

        private final Long storeId = 1L;

        @Nested
        @DisplayName("성공")
        class SuccessCase {
            @Test
            @DisplayName("매장 삭제 성공")
            void deleteStore_Success() {
                // given
                when(storeMapper.findById(storeId)).thenReturn(Optional.of(StoreDto.builder()
                        .id(storeId)
                        .build()));
                // when
                storeService.deleteStore(storeId);
                when(storeMapper.findById(storeId)).thenReturn(Optional.empty());
                // then
                Assertions.assertThrows(StoreException.class, () -> {
                    storeService.deleteStore(storeId);
                });
            }

        }

        @Nested
        @DisplayName("실패")
        class FailCase {
            private final Long storeId = 1L;

            @Test
            @DisplayName("존재하지 않는 경우")
            void deleteStore_Fail_NotFound() {
                // given
                when(storeMapper.findById(storeId)).thenReturn(Optional.empty());
                // when
                StoreException ex = Assertions.assertThrows(StoreException.class, () -> {
                    storeService.deleteStore(storeId);
                });
                // then
                Assertions.assertEquals(ex.getMessage(), "존재하지 않는 매장입니다");
            }
        }
    }

    @Nested
    @DisplayName("매장 상태 변경")
    class changeStatus {
        private final Long storeId = 1L;

        @Nested
        @DisplayName("성공")
        class SuccessCase {

            @Test
            @DisplayName("상태변경 성공")
            void changeStatus_Success() {
                // given
                StoreDto storeDto = StoreDto.builder().status(StoreStatus.OPEN).build();
                when(storeMapper.findById(storeId)).thenReturn(Optional.of(StoreDto.builder().status(StoreStatus.CLOSED).build()));
                // when
                storeService.changeStatus(storeId, storeDto.getStatus());
                when(storeMapper.findById(storeId)).thenReturn(Optional.of(StoreDto.builder().status(StoreStatus.OPEN).build()));
                Optional<StoreDto> afterStore = storeMapper.findById(storeId);
                // then
                Assertions.assertEquals(afterStore.get().getStatus(), storeDto.getStatus());
            }
        }

        @Nested
        @DisplayName("실패")
        class FailCase {
            @Test
            @DisplayName("매장이 존재하지 않는경우")
            void changeStatus_Fail_NotFound() {
                // given
                when(storeMapper.findById(storeId)).thenReturn(Optional.empty());
                // when
                StoreException ex = Assertions.assertThrows(StoreException.class, () -> {
                    storeService.deleteStore(storeId);
                });
                // then
                Assertions.assertEquals(ex.getMessage(), "존재하지 않는 매장입니다");
            }
        }
    }


}