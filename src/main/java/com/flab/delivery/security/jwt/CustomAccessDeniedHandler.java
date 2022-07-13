package com.flab.delivery.security.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.delivery.exception.AuthorizationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        log.error("인가 예외 발생 : " + e.getMessage());

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setHeader("content-type", MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(
                new AuthorizationException("권한이 없습니다.", HttpStatus.FORBIDDEN)));

        response.getWriter().flush();
        response.getWriter().close();
    }
}
