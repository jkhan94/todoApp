package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "스케줄 응답")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponseDto {
    @Schema(description = "스케줄 아이디(고유번호)", name = "id", type = "Long", example = "1")
    private Long id;

    @Schema(description = "스케줄 제목", name = "title", type = "String", example = "API 명세서 작성")
    private String title;

    @Schema(description = "스케줄 내용", name = "contents", type = "String", example = "열심히 써봅니다.")
    private String contents;

    @Schema(description = "담당자(이메일 형식)", name = "responsibility", type = "String", example = "apiSpecification@example.com")
    private String responsibility;

    @Schema(description = "스케줄 작성일", name = "createdDate", type = "Timestamp", example = "2024-05-27 16:45:04.570942")
    private Timestamp createdDate;

    @Schema(description = "스케줄에 달린 댓글 목록", name = "comments", type = "List<CommentResponseDto>")
    private List<CommentResponseDto> comments;

    @Schema(description = "스케줄을 작성한 유저의 고유번호", name = "userId", type = "Long", example = "1")
    private Long userId;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.contents = schedule.getContents();
        this.responsibility = schedule.getResponsibility();
        this.createdDate = schedule.getCreatedDate();
        this.comments = schedule.getCommentList().stream().map(CommentResponseDto::new).collect(Collectors.toList());
        this.userId = schedule.getUserSchedule().getId();
    }
}
