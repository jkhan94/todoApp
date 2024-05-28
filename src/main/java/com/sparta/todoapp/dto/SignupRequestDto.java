package com.sparta.todoapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @NotBlank
    private String nickname;

    // 4~10자, 알파벳 소문자(a~z), 숫자(0~9)`로 구성
    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    @NotBlank
    private String username;

    // 8-15자, 알파벳 대소문자(a~z, A~Z), 숫자(0~9)
    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$")
    @NotBlank
    private String password;

    private boolean admin = false;
    private String adminToken = ""; // 공백
}
