package com.flab.delivery.service;

import com.flab.delivery.dao.FCMTokenDao;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FCMServiceTest {

    @InjectMocks
    FCMService fcmService;

    @Mock
    FCMTokenDao fcmTokenDao;

    String userId = "user1";
    String token = "1111";

    @Test
    @DisplayName("로그인시 토큰 저장")
    void save() {
        fcmService.saveToken(userId, token);
        verify(fcmTokenDao, times(1)).save(userId, token);
    }

    @Test
    @DisplayName("토큰 삭제")
    void deleteToken() {
        fcmService.deleteToken(userId);
        verify(fcmTokenDao, times(1)).delete(userId);
    }


}