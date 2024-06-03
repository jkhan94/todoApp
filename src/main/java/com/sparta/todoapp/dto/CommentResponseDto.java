package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Schema(description = "댓글 응답")
@Getter
@NoArgsConstructor
public class CommentResponseDto {
    @Schema(description = "댓글 고유번호", name = "id", type = "Long", example = "1")
    private Long id;

    @Schema(description = "댓글 내용", name = "contents", type = "String", example = "나는 댓글 내용입니다.")
    private String contents;

    @Schema(description = "댓글을 작성한 유저의 고유번호", name = "userId", type = "Long", example = "1")
    private Long userId;

    @Schema(description = "댓글이 달린 스케줄의 고유번호", name = "scheduleId", type = "Long", example = "1")
    private Long scheduleId;

    @Schema(description = "댓글 작성일", name = "createdDate", type = "Timestamp", example = "2024-05-27 17:06:55.814454")
    private Timestamp createdDate;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.userId = comment.getUser().getId();
        this.scheduleId = comment.getSchedule().getId();
        this.createdDate = comment.getCreatedDate();
    }
}
