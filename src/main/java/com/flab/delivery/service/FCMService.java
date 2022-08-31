package com.flab.delivery.service;

import com.flab.delivery.dao.FCMTokenDao;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class FCMService {

    private final FCMTokenDao fcmTokenDao;

    public void saveToken(String userId, String token) {
        fcmTokenDao.save(userId, token);
    }

    public void deleteToken(String userId) {
        fcmTokenDao.delete(userId);
    }

    private Message getMessage(String token, String title, String message) {
        return Message.builder()
                .setToken(token)
                .putData("title", title)
                .putData("message", message)
                .build();
    }

    @Async
    public void sendMessage(String userId, String title, String message) {
        String token = fcmTokenDao.findToken(userId);
        if (token == null) {
            return;
        }
        Message msg = getMessage(token, title, message);
        try {
            FirebaseMessaging.getInstance().send(msg);
        } catch (FirebaseMessagingException ex) {
            log.error("FCM 메시지 전송 오류: {}", ex.getMessage());
            fcmTokenDao.delete(userId);
        }
    }


}
