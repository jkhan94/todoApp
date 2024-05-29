package com.sparta.todoapp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/* 400: bad request
 * 200 ok
 * 404 not found
 * */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ErrorEnum {
    NOT_VALID_ARGUMENTS(400, "입력값을 확인해주세요."),

    // Schedule
    SCHEDULE_NOT_FOUND(400, "등록되지 않은 스케줄입니다."),
    BAD_PASSWORD(400, "비밀번호가 틀립니다."),

    //Comment
    // 토큰이 있고, 유효한 토큰이지만 해당 사용자가 작성한 게시글/댓글이 아닐 때
    NOT_AVAILABLE_USER(400, "작성자만 삭제/수정할 수 있습니다."),
    COMMENT_NOT_FOUND(400, "등록되지 않은 댓글입니다."),
    NOT_SAVED(400, "디비 저장에 실패했습니다."),

    // User
    // DB에 이미 존재하는 `username`으로 회원가입을 요청할 때
    USERNAME_ALREADY_EXISTS(400, "중복된 username 입니다."),
    // 로그인 시, 전달된 `username`과 `password` 중 맞지 않는 정보가 있을 때
    USER_NOT_FOUND(400, "회원을 찾을 수 없습니다."),
    // 토큰이 필요한 API 요청에서 토큰을 전달하지 않았거나 정상 토큰이 아닐 때
    BAD_TOKEN(400, "토큰이 유효하지 않습니다."),
    // 관리자 계정으로 회원가입 실패
    WRONG_ADMIN_TOKEN(400, "관리자 암호가 틀립니다."),
    LOGIN_FAIL(400, "로그인 실패!"),

    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, "서버 에러입니다. 서버 팀에 연락주세요!");

    int statusCode;
    //    String code;
    String msg; // 출력 메시지
}
