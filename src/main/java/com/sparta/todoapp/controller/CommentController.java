package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.exception.CommonResponse;
import com.sparta.todoapp.exception.CustomException;
import com.sparta.todoapp.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.todoapp.exception.ErrorEnum.*;

// Swagger 링크: http://localhost:8080/swagger-ui/index.html#/
@Tag(name = "Comment CRUD", description = "댓글 등록, 조회, 수정, 삭제 컨트롤러")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "선택한 일정에 댓글 등록", description = "댓글 내용을 전달받아 디비에 저장")
    @Parameters({
            @Parameter(name = "scheduleId", description = "댓글을 등록할 스케줄의 고유번호"),
            @Parameter(name = "requestDto", description = "클라이언트가 입력한 댓글 정보"),
            @Parameter(name = "bindingResult", description = "형식에 맞지 않는 입력값 때문에 발생하는 에러 저장"),
            @Parameter(name = "req", description = "HTTP 요청에 저장된 유저 정보 가져올 때 사용")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글이 등록되었습니다."),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요.<br> 등록되지 않은 스케줄입니다.<br>디비 저장에 실패했습니다.<br>" +
                    "토큰이 유효하지 않습니다.<br>회원을 찾을 수 없습니다.")
//            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요."),
//            @ApiResponse(responseCode = "400", description = "등록되지 않은 스케줄입니다."),
//            @ApiResponse(responseCode = "400", description = "디비 저장에 실패했습니다."),
//            @ApiResponse(responseCode = "400", description = "토큰이 유효하지 않습니다."),
//            @ApiResponse(responseCode = "400", description = "회원을 찾을 수 없습니다.")
    })
    @PostMapping("/{scheduleId}")
    public ResponseEntity<CommonResponse> createComment(@PathVariable Long scheduleId, @RequestBody @Valid CommentRequestDto requestDto,
                                                        BindingResult bindingResult, HttpServletRequest req) {
        validateScheduleId(scheduleId);

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            throw new CustomException(NOT_VALID_ARGUMENTS);
        }

        CommentResponseDto comment = commentService.createComment(scheduleId, requestDto, req);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .msg("댓글이 등록되었습니다.")
                .data(comment)
                .build());
    }


    @Operation(summary = "모든 댓글 조회", description = "디비에 저장된 모든 댓글 조회")
    @ApiResponse(responseCode = "200", description = "등록된 모든 댓글 조회.")
    @GetMapping("/read")
    public List<CommentResponseDto> getComment() {
        return commentService.findCommentById();
    }


    @Operation(summary = "댓글 수정", description = "선택한 댓글의 내용만 수정")
    @Parameters({
            @Parameter(name = "scheduleId", description = "댓글을 등록할 스케줄의 고유번호"),
            @Parameter(name = "commentId", description = "수정할 댓글의 고유번호"),
            @Parameter(name = "requestDto", description = "클라이언트가 입력한 댓글 정보"),
            @Parameter(name = "bindingResult", description = "형식에 맞지 않는 입력값 때문에 발생하는 에러 저장"),
            @Parameter(name = "req", description = "HTTP 요청에 저장된 유저 정보 가져올 때 사용")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글이 수정되었습니다."),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요.<br> 등록되지 않은 댓글입니다.<br>등록되지 않은 스케줄입니다.<br>" +
                    "작성자만 삭제/수정할 수 있습니다.<br>디비 저장에 실패했습니다.<br>" +
                    "토큰이 유효하지 않습니다.<br>회원을 찾을 수 없습니다.")
//            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요."),
//            @ApiResponse(responseCode = "400", description = "등록되지 않은 댓글입니다."),
//            @ApiResponse(responseCode = "400", description = "등록되지 않은 스케줄입니다."),
//            @ApiResponse(responseCode = "400", description = "작성자만 삭제/수정할 수 있습니다."),
//            @ApiResponse(responseCode = "400", description = "디비 저장에 실패했습니다."),
//            @ApiResponse(responseCode = "400", description = "토큰이 유효하지 않습니다."),
//            @ApiResponse(responseCode = "400", description = "회원을 찾을 수 없습니다.")
    })
    @PutMapping("/{scheduleId}/{commentId}")
    public ResponseEntity<CommonResponse> updateComment(@PathVariable Long scheduleId, @PathVariable Long commentId,
                                                        @RequestBody @Valid CommentRequestDto requestDto, BindingResult bindingResult,
                                                        HttpServletRequest req) {
        validateScheduleId(scheduleId);
        validateCommentId(commentId);

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            throw new CustomException(NOT_VALID_ARGUMENTS);
        }

        CommentResponseDto comment = commentService.updateComment(scheduleId, commentId, requestDto, req);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .msg("댓글이 수정되었습니다.")
                .data(comment)
                .build());
    }


    @Operation(summary = "댓글 삭제", description = "선택한 댓글을 삭제")
    @Parameters({
            @Parameter(name = "scheduleId", description = "댓글을 등록할 스케줄의 고유번호"),
            @Parameter(name = "commentId", description = "수정할 댓글의 고유번호"),
            @Parameter(name = "req", description = "HTTP 요청에 저장된 유저 정보 가져올 때 사용")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글이 삭제되었습니다."),
            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요.<br> 등록되지 않은 댓글입니다.<br>등록되지 않은 스케줄입니다.<br>" +
                    "작성자만 삭제/수정할 수 있습니다.<br>디비 저장에 실패했습니다.<br>" +
                    "토큰이 유효하지 않습니다.<br>회원을 찾을 수 없습니다.")
//            @ApiResponse(responseCode = "400", description = "입력값을 확인해주세요."),
//            @ApiResponse(responseCode = "400", description = "등록되지 않은 댓글입니다."),
//            @ApiResponse(responseCode = "400", description = "등록되지 않은 스케줄입니다."),
//            @ApiResponse(responseCode = "400", description = "작성자만 삭제/수정할 수 있습니다."),
//            @ApiResponse(responseCode = "400", description = "디비 저장에 실패했습니다."),
//            @ApiResponse(responseCode = "400", description = "토큰이 유효하지 않습니다."),
//            @ApiResponse(responseCode = "400", description = "회원을 찾을 수 없습니다.")
    })
    @DeleteMapping("/{scheduleId}/{commentId}")
    public ResponseEntity<CommonResponse> deleteComment(@PathVariable Long scheduleId, @PathVariable Long commentId,
                                                        HttpServletRequest req) {
        validateScheduleId(scheduleId);
        validateCommentId(commentId);

        commentService.deleteComment(scheduleId, commentId, req);

        return ResponseEntity.ok().body(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .msg("댓글이 삭제되었습니다.")
                .build());
    }


    private static void validateScheduleId(Long scheduleId) {
        if (scheduleId == null) {
            throw new CustomException(SCHEDULE_NOT_FOUND);
        }
    }

    private void validateCommentId(Long commentId) {
        if (commentId == null) {
            throw new CustomException(COMMENT_NOT_FOUND);
        }
    }

}
