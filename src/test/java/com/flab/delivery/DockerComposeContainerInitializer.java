package com.flab.delivery;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class DockerComposeContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final DockerComposeContainer dockerComposeContainer;
    private static final String REDIS = "redis";
    private static final int REDIS_PORT = 6379;
    public static final String MYSQL = "mysql";
    public static final int MYSQL_PORT = 3307;
    private Map<String, String> registry = new HashMap<>();


    static {
        dockerComposeContainer = new DockerComposeContainer(new File("docker-compose-test.yml"))
                .withExposedService(REDIS, REDIS_PORT, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)))
                .withExposedService(MYSQL, MYSQL_PORT, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)));

        dockerComposeContainer.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        addOverrideProperties();

        TestPropertyValues.of(registry).applyTo(applicationContext.getEnvironment());
    }

    private void addOverrideProperties() {
        String redisHost = dockerComposeContainer.getServiceHost(REDIS, REDIS_PORT);
        changeProperties("spring.redis.cache.host", redisHost);
        changeProperties("spring.redis.session.host", redisHost);
        changeProperties("spring.redis.rider.host", redisHost);
        changeProperties("spring.redis.cart.host", redisHost);
        changeProperties("spring.redis.fcm.host", redisHost);

        String redisPort = String.valueOf(dockerComposeContainer.getServicePort(REDIS, REDIS_PORT));
        changeProperties("spring.redis.cache.port", redisPort);
        changeProperties("spring.redis.session.port", redisPort);
        changeProperties("spring.redis.rider.port", redisPort);
        changeProperties("spring.redis.cart.port", redisPort);
        changeProperties("spring.redis.fcm.port", redisPort);

        changeProperties("spring.datasource.url", "jdbc:mysql://" +
                dockerComposeContainer.getServiceHost(MYSQL, MYSQL_PORT) +
                ":" +
                dockerComposeContainer.getServicePort(MYSQL, MYSQL_PORT) +
                "/home_delivery?allowMultiQueries=true");
    }

    private void changeProperties(String key, String dockerComposeContainer) {
        registry.put(key, dockerComposeContainer);
    }
}
