package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.Schedule;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private String contents;
    private Long userId;
}
