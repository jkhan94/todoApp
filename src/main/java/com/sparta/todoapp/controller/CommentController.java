package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.exception.CommonResponse;
import com.sparta.todoapp.exception.CustomException;
import com.sparta.todoapp.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
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
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
@Tag(name = "Comment CRUD", description = "댓글 등록, 조회, 수정, 삭제 컨트롤러")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{scheduleId}")
    @Operation(summary = "선택한 일정에 댓글 등록", description = "고유번호, 댓글 내용, 댓글을 작성한 사용자 아이디, 댓글이 작성된 일정 아이디, 작성일을 전달받아 디비에 저장")
//    @Parameter(name = "contents", description = "댓글 내용", example = "요구사항에 맞춰서 댓글 CRUD 구현 완료")
    public ResponseEntity<CommonResponse> createComment(@PathVariable Long scheduleId, @RequestBody @Valid CommentRequestDto requestDto,
                                                        BindingResult bindingResult, HttpServletRequest req) {
        validateScheduleId(scheduleId);

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
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

    @GetMapping("/read")
    @Operation(summary = "댓글 조회", description = "디비에 저장된 댓글의 고유번호, 내용, 작성한 사용자 아이디, 선택된 일정 아이디, 작성일을 조회")
    public List<CommentResponseDto> getComment() {
        return commentService.findCommentById();
    }

    @PutMapping("/{scheduleId}/{commentId}")
    @Operation(summary = "댓글 수정", description = "선택한 일정의 댓글 내용만 수정")
    public ResponseEntity<CommonResponse> updateComment(@PathVariable Long scheduleId, @PathVariable Long commentId,
                                                        @RequestBody @Valid CommentRequestDto requestDto, BindingResult bindingResult,
                                                        HttpServletRequest req) {
        validateScheduleId(scheduleId);
        validateCommentId(commentId);

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
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

    @DeleteMapping("/{scheduleId}/{commentId}")
    @Operation(summary = "댓글 삭제", description = "선택한 일정의 댓글을 삭제")
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
