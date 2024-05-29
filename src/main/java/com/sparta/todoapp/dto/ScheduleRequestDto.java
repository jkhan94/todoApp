package com.sparta.todoapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Schema(description = "스케줄 요청. 모든 입력값은 공백을 허용하지 않음.")
@Getter
public class ScheduleRequestDto {
    @Schema(description = "스케줄 제목", name = "title", type = "String", example = "API 명세서 작성")
    @NotBlank
    @Pattern(regexp = "^(.+){0,200}")
    private String title;

    @Schema(description = "스케줄 내용", name = "contents", type = "String", example = "열심히 써봅니다.")
    @NotBlank
    private String contents;

    @Schema(description = "담당자(이메일 형식)", name = "responsibility", type = "String", example = "apiSpecification@example.com")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotBlank
    private String responsibility;

    @Schema(description = "스케줄 수정, 삭제할 떄 사용할 비밀번호", name = "password", type = "String", example = "1234")
    @NotBlank
    private String password;
}
