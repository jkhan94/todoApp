package com.sparta.todoapp.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.util.Base64;

@Configuration
public class JwtConfig {
    // 쿠키 이름 (헤더의 키값으로 쓸 수도 있음)
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자: 한 칸 뛴다.
    public static final String BEARER_PREFIX = "Bearer ";

    // 토큰 만료시간
    @Value("${jwt.expiration}")
    private Long expiration; // 60분

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;

    public static long TOKEN_TIME;
    public static Key key;
    public static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 시크릿키, 만료시간 가져옴
    // 환경변수로 설정 후, application.properties에 정의 > @Value("${환경변수명}") > 값 대입
    @PostConstruct
    public void init() {
        TOKEN_TIME = expiration;
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }
}
