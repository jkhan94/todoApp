package com.sparta.todoapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    // 최대 200자 이내로 제한, 필수값 처리
    @NotBlank
    @Pattern(regexp = "^(.+){0,200}")
    private String title;

    @NotBlank
    private String contents;

    // 이메일 형식
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotBlank
    private String responsibility;

    @NotBlank
    private String password;
}
