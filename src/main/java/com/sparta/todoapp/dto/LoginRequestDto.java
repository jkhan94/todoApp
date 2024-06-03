package com.sparta.todoapp.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Schema(description = "로그인 요청")
@Getter
public class LoginRequestDto {
    @Schema(description = "계정 아이디", name = "username", type = "String", example = "admin")
    @NotBlank
    private String username;

    @Schema(description = "계정 비밀번호", name = "password", type = "String", example = "administrator")
    @NotBlank
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
