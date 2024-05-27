package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String responsibility;
//    private String password;
    private Timestamp createdDate;
    private List<CommentResponseDto> comments;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.contents = schedule.getContents();
        this.responsibility = schedule.getResponsibility();
//        this.password = schedule.getPassword();
        this.createdDate = schedule.getCreatedDate();
        this.comments = schedule.getCommentList().stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }
}
