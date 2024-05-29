package com.sparta.todoapp.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "클라이언트에 반환할 응답 구조화")
@Getter
@Setter
@Builder
public class CommonResponse<T> {
    @Schema(description = "HTTP 상태코드", name = "statusCode", type = "int")
    private int statusCode;

    @Schema(description = "요청 성공하면 성공 메시지, 실패하면 에러 메시지 반환", name = "msg", type = "String")
    private String msg;

    @Schema(description = "클라이언트에 반환할 데이터", name = "data", type = "T")
    private T data;
}
