package com.flab.delivery.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;


@Configuration
public class RedisFCMTokenConfig {
    @Value("${spring.redis.fcm.host}")
    private String redisHost;

    @Value("${spring.redis.fcm.port}")
    private int redisPort;

    @Bean("redisFCMTokenConnectionFactory")
    public RedisConnectionFactory redisFCMTokenConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
    }

    @Bean
    public StringRedisTemplate redisFCMTokenTemplate(@Qualifier("redisFCMTokenConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringRedisTemplate;
    }
}
