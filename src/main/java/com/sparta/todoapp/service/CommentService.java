package com.sparta.todoapp.service;

import com.sparta.todoapp.Repository.CommentRepository;
import com.sparta.todoapp.Repository.ScheduleRepository;
import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.dto.ScheduleResponseDto;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid schedule"));
        Comment comment = new Comment(requestDto);
        comment.setSchedule(schedule);
        Comment saveComment = commentRepository.save(comment);
        CommentResponseDto responseDto = new CommentResponseDto(saveComment);
        return responseDto;
    }

    public List<CommentResponseDto> getComment() {
        return commentRepository.findAll().stream().map(CommentResponseDto::new).toList();
    }

    public CommentResponseDto updateComment(Long id, ScheduleRequestDto requestDto) {
        return null;
    }

    public void deleteComment(Long id, ScheduleRequestDto requestDto) {

    }
}
