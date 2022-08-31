package com.flab.delivery.service;

import com.flab.delivery.dto.menu.MenuDto;
import com.flab.delivery.dto.menu.MenuRequestDto;
import com.flab.delivery.enums.MenuStatus;
import com.flab.delivery.exception.MenuException;
import com.flab.delivery.mapper.MenuMapper;
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
class MenuServiceTest {

    @InjectMocks
    MenuService menuService;

    @Mock
    MenuMapper menuMapper;


    @Nested
    @DisplayName("메뉴 생성")
    class CreateMenu {
        private Long menuGroupId = 1L;
        private String name = "후라이드 치킨";
        private String info = "바삭한 후라이드 치킨입니다";
        private Integer price = 18000;

        private Long storeId = 1L;

        private MenuRequestDto getMenuRequestDto() {
            return MenuRequestDto.builder()
                    .menuGroupId(menuGroupId)
                    .name(name)
                    .info(info)
                    .price(price)
                    .build();
        }

        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("생성 성공")
            void success() {
                MenuRequestDto menuRequestDto = getMenuRequestDto();
                when(menuMapper.existsByName(menuRequestDto.getMenuGroupId(), menuRequestDto.getName()))
                        .thenReturn(Optional.empty());
                menuService.createMenu(menuRequestDto, storeId);

                when(menuMapper.existsByName(menuRequestDto.getMenuGroupId(), menuRequestDto.getName()))
                        .thenReturn(Optional.of(1L));
                MenuException ex = Assertions.assertThrows(MenuException.class, () -> menuService.createMenu(menuRequestDto, storeId));
                Assertions.assertEquals(ex.getMessage(), "이미 존재하는 메뉴 입니다");
            }
        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @Test
            @DisplayName("이미 존재하는 경우")
            void exists() {
                MenuRequestDto menuRequestDto = getMenuRequestDto();
                when(menuMapper.existsByName(menuRequestDto.getMenuGroupId(), menuRequestDto.getName()))
                        .thenReturn(Optional.of(1L));

                MenuException ex = Assertions.assertThrows(MenuException.class, () -> menuService.createMenu(menuRequestDto, storeId));
                Assertions.assertEquals(ex.getMessage(), "이미 존재하는 메뉴 입니다");
            }
        }
    }

    @Nested
    @DisplayName("메뉴 한개 조회")
    class GetMenu {
        private Long id = 1L;


        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("조회 성공")
            void success() {
                // given
                MenuDto menuDto = MenuDto.builder().id(id).build();
                // when
                when(menuMapper.findById(id)).thenReturn(Optional.of(menuDto));
                MenuDto findMenu = menuService.getMenu(id);
                // then
                Assertions.assertEquals(menuDto.getId(), findMenu.getId());
            }

        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @Test
            @DisplayName("존재하지 않는경우")
            void notExists() {
                // given
                // when
                when(menuMapper.findById(id)).thenReturn(Optional.empty());
                MenuException ex = Assertions.assertThrows(MenuException.class, () -> menuService.getMenu(id));
                // then
                Assertions.assertEquals(ex.getMessage(), "존재하지 않는 메뉴입니다");
            }
        }
    }

    @Nested
    @DisplayName("메뉴 정보변경")
    class UpdateMenu {
        private Long id = 1L;

        private Long menuGroupId = 1L;
        private String name = "후라이드 치킨2";
        private String info = "바삭한 후라이드 치킨입니다";
        private Integer price = 18000;

        private Long storeId = 1L;

        private MenuRequestDto getMenuRequestDto() {
            return MenuRequestDto.builder()
                    .menuGroupId(menuGroupId)
                    .name(name)
                    .info(info)
                    .price(price)
                    .build();
        }

        @Test
        @DisplayName("업데이트 성공")
        void success() {
            // given
            String beforeName = "후라이드치킨1";
            when(menuMapper.findById(id)).thenReturn(Optional.of(MenuDto.builder().id(id).name(beforeName).build()));
            // when
            menuService.updateMenu(id, getMenuRequestDto(), storeId);
            when(menuMapper.findById(id)).thenReturn(Optional.of(MenuDto.builder().id(id).name(name).build()));
            MenuDto findMenu = menuService.getMenu(id);
            // then
            Assertions.assertNotEquals(beforeName, findMenu.getName());
        }

    }

    @Nested
    @DisplayName("메뉴 삭제")
    class DeleteMenu {
        private Long id = 1L;
        private Long storeId = 1L;

        @Test
        @DisplayName("삭제 성공")
        void success() {
            when(menuMapper.findById(id)).thenReturn(Optional.of(MenuDto.builder().id(id).build()));
            menuService.deleteMenu(id, storeId);
            when(menuMapper.findById(id)).thenReturn(Optional.empty());
            Assertions.assertThrows(MenuException.class, () -> menuService.getMenu(id));
        }
    }

    @Nested
    @DisplayName("메뉴 상태 변경")
    class UpdateStatus {

        private Long id = 1L;
        private MenuStatus menuStatus = MenuStatus.SOLDOUT;
        private Long storeId = 1L;

        @Test
        @DisplayName("상태 변경 성공 - ONSALE -> SOLDOUT")
        void success() {
            when(menuMapper.findById(id)).thenReturn(Optional.of(MenuDto.builder().id(id).status(MenuStatus.ONSALE).build()));
            MenuDto before = menuMapper.findById(id).get();
            menuService.updateStatus(id, menuStatus, storeId);
            when(menuMapper.findById(id)).thenReturn(Optional.of(MenuDto.builder().id(id).status(menuStatus).build()));
            MenuDto after = menuMapper.findById(id).get();

            Assertions.assertNotEquals(before.getStatus(), after.getStatus());
        }

    }

    @Nested
    @DisplayName("우선순위 변경")
    class UpdatePriority {

        @Nested
        @DisplayName("성공")
        class Success {
            @Test
            @DisplayName("우선순위 변경 성공")
            void success(@Mock List<MenuDto> updateList, @Mock List<MenuDto> findList) {
                int updateListSize = 2;
                int findListSize = 2;

                when(menuMapper.findAllById(updateList)).thenReturn(findList);
                when(updateList.size()).thenReturn(updateListSize);
                when(findList.size()).thenReturn(findListSize);

                menuService.updatePriority(updateList);
                verify(menuMapper).updatePriority(updateList);
            }

        }

        @Nested
        @DisplayName("실패")
        class Fail {
            @Test
            @DisplayName("존재하지않는 메뉴가있는경우")
            void notExists(@Mock List<MenuDto> updateList, @Mock List<MenuDto> findList) {
                int updateListSize = 2;
                int findListSize = 3;

                when(menuMapper.findAllById(updateList)).thenReturn(findList);
                when(updateList.size()).thenReturn(updateListSize);
                when(findList.size()).thenReturn(findListSize);

                MenuException ex = Assertions.assertThrows(MenuException.class, () -> menuService.updatePriority(updateList));
                Assertions.assertEquals(ex.getMessage(), "업데이트 중 존재하지 않는 id가 있습니다");

            }
        }
    }

    @Nested
    @DisplayName("메뉴 조회 - 그룹정렬, 메뉴정렬")
    class GetMenuList {
        @Test
        @DisplayName("조회 성공")
        void success() {
            Long storeId = 1L;
            menuService.getMenuList(storeId);
            verify(menuMapper).findAllByStoreId(storeId);
        }
    }
}