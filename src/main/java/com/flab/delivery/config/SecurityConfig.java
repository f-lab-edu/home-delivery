package com.flab.delivery.config;

import com.flab.delivery.security.jwt.CustomAccessDeniedHandler;
import com.flab.delivery.security.jwt.CustomAuthenticationEntryPoint;
import com.flab.delivery.security.jwt.JwtAuthenticationFilter;
import com.flab.delivery.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // PreAuthorize 사용하기 위해 설정
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtProvider jwtProvider;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // 기본 인증 로그인 사용 X
                .csrf().disable() // Rest API 로 CSRF 비활성화
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 비활성화
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint) // 401 인증 오류에 대한 처리
                .accessDeniedHandler(customAccessDeniedHandler); // 403 인가 오류에 대한 처리


        http.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
    }
}
