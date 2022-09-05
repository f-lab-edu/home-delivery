package com.flab.delivery.dao;


import com.flab.delivery.annotation.IntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@IntegrationTest
public class FCMTokenDaoTest {

    @Autowired
    FCMTokenDao fcmTokenDao;

    String userId = "user1";
    String token = "11231456";

    @AfterEach
    void afterEach() {
        fcmTokenDao.delete(userId);
    }

    @Test
    @DisplayName("토큰 가져오기")
    void findToken() {
        String findToken = fcmTokenDao.findToken(userId);
        Assertions.assertNull(findToken);
    }


    @Test
    @DisplayName("저장")
    void save() {
        fcmTokenDao.save(userId, token);
        String findToken = fcmTokenDao.findToken(userId);
        Assertions.assertEquals(token, findToken);
    }

    @Test
    @DisplayName("토큰 삭제")
    void delete() {
        fcmTokenDao.save(userId, token);
        fcmTokenDao.delete(userId);
        String findToken = fcmTokenDao.findToken(userId);
        Assertions.assertNull(findToken);
    }


}
