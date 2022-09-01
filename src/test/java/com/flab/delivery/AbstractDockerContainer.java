package com.flab.delivery;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.time.Duration;

public abstract class AbstractDockerContainer {
    private static final DockerComposeContainer dockerComposeContainer;

    private static final String REDIS = "redis";

    private static final int REDIS_PORT = 6379;

    public static final String MYSQL = "mysql";

    public static final int MYSQL_PORT = 3307;

    static {

        dockerComposeContainer =
                new DockerComposeContainer(new File("docker-compose-test.yml"))
                        .withExposedService(REDIS, REDIS_PORT, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)))
                        .withExposedService(MYSQL, MYSQL_PORT, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)));
        dockerComposeContainer.start();
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.cache.host", () -> dockerComposeContainer.getServiceHost(REDIS, REDIS_PORT));
        registry.add("spring.redis.session.host", () -> dockerComposeContainer.getServiceHost(REDIS, REDIS_PORT));
        registry.add("spring.redis.rider.host", () -> dockerComposeContainer.getServiceHost(REDIS, REDIS_PORT));

        registry.add("spring.redis.cache.port", () -> dockerComposeContainer.getServicePort(REDIS, REDIS_PORT));
        registry.add("spring.redis.session.port", () -> dockerComposeContainer.getServicePort(REDIS, REDIS_PORT));
        registry.add("spring.redis.rider.port", () -> dockerComposeContainer.getServicePort(REDIS, REDIS_PORT));

        registry.add("spring.datasource.url", () ->
                "jdbc:mysql://" +
                        dockerComposeContainer.getServiceHost(MYSQL, MYSQL_PORT) +
                        ":" +
                        dockerComposeContainer.getServicePort(MYSQL, MYSQL_PORT) +
                        "/home_delivery?allowMultiQueries=true");
    }

}