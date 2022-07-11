package com.flab.delivery.security.jwt;

import com.flab.delivery.dao.TokenDao;
import com.flab.delivery.dto.TokenDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.dto.UserDto.AuthDto;
import com.flab.delivery.exception.CertifyException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

/**
 * Json Web Token (Jwt) 생성 및 유효성 검증을 하는 컴포넌트
 * Claim : 회원을 구분할 수 있는 값을 세팅
 * resolveToken : header 에 세팅된 토큰값을 가져와서 유효성을 검사
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;


    @Value("${jwt.accessTokenValidTime}")
    private int accessTokenValidTime;

    @Value("${jwt.refreshTokenValidTime}")
    private int refreshTokenValidTime;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * JWT Token 생성
     */
    public TokenDto createToken(UserDto userDto) {
        return createToken(userDto.getId(), userDto.getLevel());
    }

    public TokenDto createToken(String userId, String level) {

        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("level", level);

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + (accessTokenValidTime * 1000)))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + (refreshTokenValidTime * 1000)))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    /**
     * jwt 에서 회원 구분 PK 추출
     */
    public Claims parseClaims(String token) {

        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException : ", e);
            return e.getClaims();
        }
    }

    /**
     * Header 에 있는 Authorization 의 토큰 값을 가져옴
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    /**
     * Jwt 의 유효성 및 만료일자 확인
     */
    public boolean validationToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (java.lang.SecurityException | MalformedJwtException | SignatureException e) {
            log.error("잘못된 Jwt 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("잘못된 토큰입니다.");
        }
        return false;
    }

    /**
     * 토큰 만료시간 확인
     */
    public boolean isExpiredToken(String token) {

        Claims claims = parseClaims(token);

        return claims.getExpiration().getTime() - System.currentTimeMillis() < 0;
    }

    /**
     * Reissue 토큰 검증
     */
    public void validateTokens(TokenDao tokenDao, String accessToken, String refreshToken) {
        String userId = parseClaims(accessToken).getSubject();

        if (!validationToken(refreshToken) || !validationToken(accessToken)) {
            log.error("잘못된 토큰 갱신 요청 userId = {}", userId);
            throw getCertifyException("토큰을 갱신할 수 없습니다");
        }

        if (!isExpiredToken(accessToken)) {
            tokenDao.removeTokenByUserId(userId);
            log.error("만료되지 않은 토큰 갱신 요청 userId = {}", userId);
            throw getCertifyException("토큰을 갱신할 수 없습니다");
        }
    }

    private CertifyException getCertifyException(String message) {
        return new CertifyException(message, HttpStatus.CONFLICT);
    }

    public AuthDto getAuthDto(String refreshToken) {
        Claims claims = parseClaims(refreshToken);
        return AuthDto.builder()
                .id(claims.getSubject())
                .level(String.valueOf(claims.get("level")))
                .build();
    }

}
