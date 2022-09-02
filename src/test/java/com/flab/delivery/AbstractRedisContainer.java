package com.flab.delivery;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;

public abstract class AbstractRedisContainer {
    private static final GenericContainer REDIS_CONTAINER;

    private static final String REDIS_IMAGE = "redis";

    static {
        REDIS_CONTAINER = new GenericContainer<>(REDIS_IMAGE)
                .withExposedPorts(6379)
                .withReuse(true);
        REDIS_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.cache.host", REDIS_CONTAINER::getHost);
        registry.add("spring.redis.session.host", REDIS_CONTAINER::getHost);
        registry.add("spring.redis.rider.host", REDIS_CONTAINER::getHost);
        registry.add("spring.cart.rider.host", REDIS_CONTAINER::getHost);
        registry.add("spring.fcm.rider.host", REDIS_CONTAINER::getHost);

        registry.add("spring.redis.cache.port", () -> REDIS_CONTAINER.getMappedPort(6379));
        registry.add("spring.redis.session.port", () -> REDIS_CONTAINER.getMappedPort(6379));
        registry.add("spring.redis.rider.port", () -> REDIS_CONTAINER.getMappedPort(6379));
        registry.add("spring.redis.cart.port", () -> REDIS_CONTAINER.getMappedPort(6379));
        registry.add("spring.redis.fcm.port", () -> REDIS_CONTAINER.getMappedPort(6379));
    }

}