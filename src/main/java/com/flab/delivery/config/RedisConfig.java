package com.flab.delivery.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Profile 을 통해 실제 운영 및 로컬 환경에서는 HttpSession 대신 레디스가 지원하는 스프링 세션을 사용합니다
 * 기존 테스트 성공을 위해 스프링 세션이 아닌 기존 HttpSession 을 통한 MockHttpSession 으로 진행하여
 * 기존에 작성한 테스트에 문제 없게 하였습니다.
 */
@Profile("local")
@Configuration
@EnableRedisHttpSession
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
    }
}
