package com.flab.delivery.config;

import com.flab.delivery.utils.LoginUserIdResolver;
import com.flab.delivery.utils.LoginUserTypeResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginUserIdResolver loginUserIdResolver;
    private final LoginUserTypeResolver loginUserTypeResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserIdResolver);
        resolvers.add(loginUserTypeResolver);
    }

}
