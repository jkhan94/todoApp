package com.sparta.todoapp.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.exception.CommonResponse;
import com.sparta.todoapp.exception.CustomException;
import com.sparta.todoapp.exception.ErrorEnum;
import com.sparta.todoapp.jwt.JwtUtil;
import com.sparta.todoapp.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

import static com.sparta.todoapp.exception.ErrorEnum.BAD_TOKEN;
import static com.sparta.todoapp.exception.ErrorEnum.USER_NOT_FOUND;

@Slf4j(topic = "AuthFilter")
@Component
@Order(2)
@RequiredArgsConstructor
public class AuthFilter implements Filter {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = httpServletRequest.getRequestURI();

        // 회원가입, 로그인, swagger, 조회 관련 API 는 인증 필요없이 요청 진행
        if (StringUtils.hasText(url)
                && (url.startsWith("/user") || url.endsWith("/read")
                || url.contains("swagger")|| url.contains("api-docs") )) {
            chain.doFilter(request, response); // 다음 Filter 로 이동
        } else {
            // 나머지 API 요청은 인증 처리 진행
            // 토큰 확인
            String tokenValue = jwtUtil.getJwtFromHeader(httpServletRequest);

            if (StringUtils.hasText(tokenValue)) { // 토큰이 존재하면 검증 시작
                // 토큰 검증
                if (!jwtUtil.validateToken(tokenValue)) {
                    jwtExceptionHandler(response, BAD_TOKEN);
//                    throw new IllegalArgumentException("Not Found Token");
                }

                // 토큰에서 사용자 정보 가져오기
                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

                User user = userRepository.findByUsername(info.getSubject()).orElseThrow(() ->
                        new CustomException(USER_NOT_FOUND)
                );

                request.setAttribute("user", user);
                chain.doFilter(request, response); // 다음 Filter 로 이동
            } else {
                jwtExceptionHandler(response, BAD_TOKEN);
            }
        }
    }

    // 토큰에 대한 오류가 발생했을 때, 커스터마이징해서 Exception 처리 값을 클라이언트에게 알려준다.
    public void jwtExceptionHandler(ServletResponse response, ErrorEnum error) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(CommonResponse.builder()
                    .statusCode(error.getStatusCode())
                    .msg(error.getMsg())
                    .build());
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public ResponseEntity<CommonResponse> handleException(CustomException ex) {
        return ResponseEntity.ok().body(CommonResponse.builder()
                .statusCode(ex.getStatusEnum().getStatusCode())
                .msg(ex.getStatusEnum().getMsg())
                .build());
    }
}
