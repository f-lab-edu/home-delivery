package com.flab.delivery.security.jwt;

import com.flab.delivery.dao.TokenDao;
import com.flab.delivery.dto.TokenDto;
import com.flab.delivery.dto.UserDto;
import com.flab.delivery.dto.UserDto.AuthDto;
import com.flab.delivery.exception.CertifyException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

/**
 * Json Web Token (Jwt) 생성 및 유효성 검증을 하는 컴포넌트
 * Claim : 회원을 구분할 수 있는 값을 세팅
 * resolveToken : header 에 세팅된 토큰값을 가져와서 유효성을 검사
 */
@Slf4j
@Component
public class JwtProvider {


    @Value("${jwt.accessTokenValidTime}")
    private int accessTokenValidTime;

    @Value("${jwt.refreshTokenValidTime}")
    private int refreshTokenValidTime;

    private final SecretKey key = Keys.secretKeyFor(HS256);
    private final JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();

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

        String accessToken = getToken(claims, now, accessTokenValidTime);
        String refreshToken = getToken(claims, now, refreshTokenValidTime);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String getToken(Claims claims, Date now, int accessTokenValidTime) {
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + (accessTokenValidTime * 1000))) // 토큰 만료시간
                .signWith(key)
                .compact();
    }


    /**
     * JWT 에서 Claims 정보를 추출
     * 만료된 토큰이라도, RefreshToken 을 통해 재발급을 위해 Claims 반환
     */
    public Claims parseClaims(String token) {

        try {
            return parser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException | IllegalArgumentException e) {
            throw new CertifyException("옳바르지 않은 토큰 입니다.", HttpStatus.UNAUTHORIZED);
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
    public boolean isValidToken(String token) {
        try {
            parser.parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException e) {
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
    public void validateTokenToReissue(TokenDao tokenDao, String accessToken, String refreshToken) {

        if (!isValidToken(refreshToken)) {
            throw getCertifyException("토큰을 갱신할 수 없습니다");
        }

        String userId = parseClaims(refreshToken).getSubject();

        if (!isExpiredToken(accessToken)) {
            tokenDao.removeTokenByUserId(userId);
            log.error("만료되지 않은 토큰 갱신 요청 userId = {}", userId);
            throw getCertifyException("토큰을 갱신할 수 없습니다");
        }
    }

    private CertifyException getCertifyException(String message) {
        return new CertifyException(message, HttpStatus.CONFLICT);
    }

    /**
     * 토큰을 통해 AuthDto 생성
     */
    public AuthDto getAuthDto(String token) {
        Claims claims = parseClaims(token);
        return AuthDto.builder()
                .id(claims.getSubject())
                .level(String.valueOf(claims.get("level")))
                .build();
    }

}
