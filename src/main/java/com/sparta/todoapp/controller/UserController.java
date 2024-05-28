package com.sparta.todoapp.controller;

import com.sparta.todoapp.CommonResponse;
import com.sparta.todoapp.dto.LoginRequestDto;
import com.sparta.todoapp.dto.SignupRequestDto;
import com.sparta.todoapp.jwt.JwtUtil;
import com.sparta.todoapp.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 로그인, 회원가입 html 페이지 반환
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signup(@ModelAttribute @Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        // bindingResult에 오류 목록 저장
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        // 오류가 발생했다면 어느 필드에서 에러가 발생했는지 출력
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
        }

        // 문제가 없다면 회원가입 시도
        userService.signup(requestDto);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .msg("가입되었습니다.")
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(LoginRequestDto requestDto, HttpServletResponse res) {
        // requestDto 검증 후 res에 JWT 담긴 쿠키 응답
        try {
            userService.login(requestDto, res);
        } catch (Exception e) {
            // 로그인 실패
            log.error("로그인에 실패했습니다.");
        }
        // 성공 시 JWT, 상태코드, 성공메시지 반환
        return ResponseEntity.ok().body(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .msg("로그인되었습니다.")
                .data(res.getHeader(JwtUtil.AUTHORIZATION_HEADER))
                .build());
    }
}
