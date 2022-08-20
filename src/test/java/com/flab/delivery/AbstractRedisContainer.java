package com.flab.delivery;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;

public abstract class AbstractRedisContainer {
    static final String REDIS_IMAGE = "redis";
    static final GenericContainer REDIS_CONTAINER;

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

        registry.add("spring.redis.cache.port", () -> "" + REDIS_CONTAINER.getExposedPorts().get(0));
        registry.add("spring.redis.session.port", () -> "" + REDIS_CONTAINER.getExposedPorts().get(0));
        registry.add("spring.redis.rider.port", () -> "" + REDIS_CONTAINER.getExposedPorts().get(0));
    }
}