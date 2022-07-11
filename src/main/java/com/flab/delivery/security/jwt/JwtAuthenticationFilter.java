package com.flab.delivery.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 이 유효한 토큰인지 인증하기 위한 Filter
 * Username and Password Authentication 을 사용하지 않기 때문에 UsernamePasswordAuthenticationFilter 앞에 세팅 해야함
 * 즉, UsernamePasswordAuthenticationFilter 가 아닌  JwtAuthenticationFilter 에서 인증 및 인가 처리
 * OncePerRequestFilter : 동일한 request 에 대하여 단 한번의 실행을 보장하여 중복 호출을 방지하여 자원 낭비를 방지한다
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtProvider.resolveToken(request);

        log.debug("[Verifying token]");
        log.debug(request.getRequestURL().toString());

        if (token != null && jwtProvider.validationToken(token)) {
            Authentication authentication = userDetailsService.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}
