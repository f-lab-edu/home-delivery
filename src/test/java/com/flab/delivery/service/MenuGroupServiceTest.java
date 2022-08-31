package com.flab.delivery.service;

import com.flab.delivery.dto.menugroup.MenuGroupDto;
import com.flab.delivery.dto.menugroup.MenuGroupRequestDto;
import com.flab.delivery.dto.store.StoreDto;
import com.flab.delivery.exception.MenuGroupException;
import com.flab.delivery.mapper.MenuGroupMapper;
import com.flab.delivery.mapper.StoreMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    MenuGroupService menuGroupService;

    @Mock
    MenuGroupMapper menuGroupMapper;


    @Nested
    @DisplayName("메뉴 그룹 생성")
    class createMenuGroup {

        private Long storeId = 1L;
        private String name = "치킨";
        private String info = "치킨 그룹입니다";

        private MenuGroupRequestDto getRequestDto() {
            return MenuGroupRequestDto.builder()
                    .storeId(storeId)
                    .name(name)
                    .info(info)
                    .build();
        }


        @Nested
        @DisplayName("성공")
        class Success {


            @Test
            @DisplayName("생성 성공")
            void success() {
                // given
                when(menuGroupMapper.existsByName(storeId, name)).thenReturn(Optional.empty());
                // when
                menuGroupService.createMenuGroup(getRequestDto());
                when(menuGroupMapper.existsByName(storeId, name)).thenReturn(Optional.of(1L));

                MenuGroupException ex = Assertions.assertThrows(MenuGroupException.class, () -> {
                    menuGroupService.createMenuGroup(getRequestDto());
                });
                // then
                Assertions.assertEquals(ex.getMessage(), "이미 존재하는 메뉴 그룹입니다");
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @Test
            @DisplayName("이미 존재하는 경우")
            void exists() {
                // given
                when(menuGroupMapper.existsByName(storeId, name)).thenReturn(Optional.of(1L));
                // when
                MenuGroupException ex = Assertions.assertThrows(MenuGroupException.class, () -> {
                    menuGroupService.createMenuGroup(getRequestDto());
                });
                // then
                Assertions.assertEquals(ex.getMessage(), "이미 존재하는 메뉴 그룹입니다");
            }

        }

    }

    @Nested
    @DisplayName("그룹 정보 변경")
    class updateMenuGroup {
        private Long storeId = 1L;
        private String name = "치킨";
        private String info = "치킨 그룹입니다";

        private Long id = 1L;

        private MenuGroupRequestDto getRequestDto() {
            return MenuGroupRequestDto.builder()
                    .storeId(storeId)
                    .name(name)
                    .info(info)
                    .build();
        }

        @Test
        @DisplayName("정보 변경성공")
        void success() {
            // given
            String beforeName = "치킨";
            name = "족발";

            when(menuGroupMapper.findById(id)).thenReturn(Optional.of(MenuGroupDto.builder()
                    .name(beforeName)
                    .build()));
            // when
            menuGroupService.updateMenuGroup(id, getRequestDto());
            when(menuGroupMapper.findById(id)).thenReturn(Optional.of(MenuGroupDto.builder()
                    .name(name)
                    .build()));
            MenuGroupDto after = menuGroupMapper.findById(id).get();
            Assertions.assertNotEquals(beforeName, after.getName());
        }

    }

    @Nested
    @DisplayName("그룹 리스트 조회")
    class getMenuGroupList {

        Long storeId = 1L;

        @Test
        @DisplayName("리스트 조회 성공")
        void success(@Mock List<MenuGroupDto> menuGroupList) {
            // given
            int size = 1;
            when(menuGroupList.size()).thenReturn(size);
            when(menuGroupService.getMenuGroupList(storeId)).thenReturn(menuGroupList);
            // when
            List<MenuGroupDto> getMenuGroupList = menuGroupService.getMenuGroupList(storeId);
            // then
            Assertions.assertEquals(size, getMenuGroupList.size());
        }

    }

    @Nested
    @DisplayName("그룹 삭제 - 메뉴도 같이 삭제됨 메뉴추가후 테스트케이스 추가해야함")
    class deleteGroup {

        private Long id = 1L;
        private Long storeId = 1L;

        @Test
        @DisplayName("삭제 성공")
        void success() {
            // given
            // when
            menuGroupService.deleteGroup(id, storeId);
            when(menuGroupMapper.findById(id)).thenReturn(Optional.empty());
            // then
            Assertions.assertFalse(menuGroupMapper.findById(id).isPresent());
        }

    }

    @Nested
    @DisplayName("우선순위 업데이트")
    class updatePriority {
        @Test
        @DisplayName("성공")
        void success(@Mock List<MenuGroupDto> requestDtoList, @Mock List<MenuGroupDto> findList) {
            int requestListSize = 2;
            int findListSize = 2;
            when(menuGroupMapper.findAllById(requestDtoList)).thenReturn(findList);
            when(requestDtoList.size()).thenReturn(requestListSize);
            when(findList.size()).thenReturn(findListSize);

            menuGroupService.updatePriority(requestDtoList);

            verify(menuGroupMapper).updatePriority(requestDtoList);
        }

        @Test
        @DisplayName("실패 아이디가 존재하지않는경우")
        void fail(@Mock List<MenuGroupDto> requestList, @Mock List<MenuGroupDto> findList) {
            int requestListSize = 2;
            int findListSize = 3;

            when(menuGroupMapper.findAllById(requestList)).thenReturn(findList);
            when(requestList.size()).thenReturn(requestListSize);
            when(findList.size()).thenReturn(findListSize);

            MenuGroupException ex = Assertions.assertThrows(MenuGroupException.class, () -> {
                menuGroupService.updatePriority(requestList);
            });

            Assertions.assertEquals(ex.getMessage(), "업데이트 중 존재하지 않는 id가 있습니다");

        }

    }
}