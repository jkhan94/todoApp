package com.sparta.todoapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "회원가입 요청")
@Getter
@Setter
public class SignupRequestDto {
    @Schema(description = "유저 별명", name = "nickname", type = "String", example = "관리자")
    @NotBlank
    private String nickname;

    @Schema(description = "계정 아이디: 4~10자, 알파벳 소문자(a~z), 숫자(0~9)`로 구성", name = "username", type = "String", example = "admin")
    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    @NotBlank
    private String username;

    @Schema(description = "계정 비밀번호: 8-15자, 알파벳 대소문자(a~z, A~Z), 숫자(0~9)", name = "password", type = "String", example = "administrator")
    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$")
    @NotBlank
    private String password;

    @Schema(description = "관리자 계정인지 확인", name = "admin", type = "boolean")
    private boolean admin = false;
    @Schema(description = "관리자 계정으로 가입하기 위해 입력해야 하는 값", name = "adminToken", type = "String")
    private String adminToken = ""; // 공백
}
