package com.sparta.todoapp.controller;

import com.sparta.todoapp.config.JwtConfig;
import com.sparta.todoapp.dto.LoginRequestDto;
import com.sparta.todoapp.dto.SignupRequestDto;
import com.sparta.todoapp.exception.CommonResponse;
import com.sparta.todoapp.exception.CustomException;
import com.sparta.todoapp.jwt.JwtUtil;
import com.sparta.todoapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.sparta.todoapp.exception.ErrorEnum.LOGIN_FAIL;
import static com.sparta.todoapp.exception.ErrorEnum.NOT_VALID_ARGUMENTS;

// 로그인, 회원가입 html 페이지 반환
@Tag(name = "User", description = "회원가입, 로그인 컨트롤러")
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "회원가입", description = "닉네임, 아이디, 비밀번호, 관리자 계정용 인증 토큰을 입력받아 디비에 신규유저로 등록")
    @Parameters({
            @Parameter(name = "requestDto", description = "클라이언트가 입력한 회원 정보"),
            @Parameter(name = "bindingResult", description = "형식에 맞지 않는 입력값 때문에 발생하는 에러 저장")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "가입되었습니다."),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요.<br> 중복된 username 입니다.<br>관리자 암호가 틀립니다."),
            @ApiResponse(responseCode = "401", description = "토큰이 유효하지 않습니다.<br>회원을 찾을 수 없습니다.")
    })
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signup(@Valid @ModelAttribute SignupRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        // bindingResult에 오류 목록 저장
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        // 오류가 발생했다면 어느 필드에서 에러가 발생했는지 출력
        if (!fieldErrors.isEmpty()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            throw new CustomException(NOT_VALID_ARGUMENTS);
        }

        // 문제가 없다면 회원가입 시도
        userService.signup(requestDto);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .msg("가입되었습니다.")
                .build());
    }


    @Operation(summary = "로그인", description = "아이디, 비밀번호를 입력받아 유저로 인증되면 JWT를 반환")
    @Parameters({
            @Parameter(name = "requestDto", description = "클라이언트가 입력한 회원 정보"),
            @Parameter(name = "bindingResult", description = "형식에 맞지 않는 입력값 때문에 발생하는 에러 저장"),
            @Parameter(name = "res", description = "인증 성공 시 HTTP 응답 헤더에 반환된 JWT 저장")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인되었습니다."),
            @ApiResponse(responseCode = "401", description = "토큰이 유효하지 않습니다.<br>회원을 찾을 수 없습니다.<br>로그인 실패! 아이디와 비밀번호를 확인해주세요.")
    })
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@Valid @ModelAttribute LoginRequestDto requestDto, BindingResult bindingResult, HttpServletResponse res) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        // 오류가 발생했다면 어느 필드에서 에러가 발생했는지 출력
        if (!fieldErrors.isEmpty()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            throw new CustomException(NOT_VALID_ARGUMENTS);
        }

        try {
            userService.login(requestDto, res);
        } catch (Exception e) {
            // 로그인 실패
            log.error("로그인에 실패했습니다.");
            throw new CustomException(LOGIN_FAIL);
        }

        // 성공 시 JWT, 상태코드, 성공메시지 반환
        return ResponseEntity.ok().body(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .msg("로그인되었습니다.")
                .data(res.getHeader(JwtConfig.AUTHORIZATION_HEADER))
                .build());
    }
}
