package com.flab.delivery.dao;



import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = FCMTokenDaoTest.ContainerPropertyInitializer.class)
public class FCMTokenDaoTest {

    @Autowired
    FCMTokenDao fcmTokenDao;

    @Container
    static GenericContainer redisContainer = new GenericContainer("redis")
            .withExposedPorts(6379);

    static class ContainerPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues.of("spring.redis.fcm.port=" + redisContainer.getMappedPort(6379))
                    .applyTo(context.getEnvironment());
        }
    }


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
