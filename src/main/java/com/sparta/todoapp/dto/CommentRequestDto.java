package com.sparta.todoapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Schema(description = "댓글 요청")
@Getter
public class CommentRequestDto {
    @Schema(description = "댓글 내용. 공백을 허용하지 않음", name = "contents", type = "String", example = "API 명세서를 Swagger로 작성합니다.")
    @NotBlank(message = "입력된 내용이 없습니다!")
    private String contents;
}
