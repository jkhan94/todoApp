package com.sparta.todoapp.jwt;

import com.sparta.todoapp.config.JwtConfig;
import com.sparta.todoapp.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

/*
 * JWT 생성
 * 1. 서버에서 만든 쿠키에 저장: 쿠키 만료기간 설정 가능, HTTP 헤더에  setCookie로 자동 저장
 * 2. 헤더에 담아 보냄: 쿠키 안 만드니까 코드 수 감소
 * => 더 좋은 건 없고, 서비스와 상황 맞춰서 선택.
 *    협업할 프론트팁과 협업해서 정하는 부분.
 *
 * 생성된 JWT를 쿠키에 저장
 *
 * 쿠키에 들어있던 JWT 토큰을 substring
 *
 * JWT 검증
 *
 * JWT에서 사용자 정보 가져오기
 * */

@Component
public class JwtUtil {
    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    // JWT 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return JwtConfig.BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(JwtConfig.AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + JwtConfig.TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(JwtConfig.key, JwtConfig.signatureAlgorithm) // 시크릿키, 암호화 알고리즘
                        .compact();
    }

    // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtConfig.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtConfig.BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // JWT 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(JwtConfig.key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            // 토큰 변조, 파괴
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(JwtConfig.key).build().parseClaimsJws(token).getBody();
    }

}
