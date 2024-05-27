package com.sparta.todoapp.controller;

import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.dto.ScheduleResponseDto;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Swagger 링크: http://localhost:8080/swagger-ui/index.html#/
@RestController
@RequestMapping("/comment")
@Tag(name = "Comment CRUD", description = "댓글 등록, 조회, 수정, 삭제 컨트롤러")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{id}")
    @Operation(summary = "선택한 일정에 댓글 등록", description = "고유번호, 댓글 내용, 댓글을 작성한 사용자 아이디, 댓글이 작성된 일정 아이디, 작성일을 전달받아 디비에 저장")
    @Parameters({
            @Parameter(name = "contents", description = "댓글 내용", example = "요구사항에 맞춰서 댓글 CRUD 구현 완료"),
            @Parameter(name = "createdDate", description = "댓글 작성일자", example = "2024-05-27"),
    })
    public CommentResponseDto createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto) {
        return commentService.createComment(id, requestDto);
    }

    @GetMapping
    @Operation(summary = "댓글 조회", description = "디비에 저장된 댓글의 고유번호, 내용, 작성한 사용자 아이디, 선택된 일정 아이디, 작성일을 조회")
    public List<CommentResponseDto> getComment() {
        return commentService.getComment();
    }

    @PutMapping("/{id}")
    @Operation(summary = "댓글 수정", description = "선택한 일정의 댓글을 수정")
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {
        return commentService.updateComment(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "댓글 삭제", description = "선택한 일정의 댓글을 삭제")
    public void deleteComment(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {
        commentService.deleteComment(id,requestDto);
    }

}
