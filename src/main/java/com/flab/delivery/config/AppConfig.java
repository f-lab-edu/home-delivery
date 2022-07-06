package com.flab.delivery.config;

import com.flab.delivery.utils.JbCrypt;
import com.flab.delivery.utils.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new JbCrypt();
    }
}
