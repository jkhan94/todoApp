package com.sparta.todoapp.service;

import com.sparta.todoapp.Repository.CommentRepository;
import com.sparta.todoapp.Repository.ScheduleRepository;
import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    public CommentResponseDto createComment(Long scheduleId, CommentRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new IllegalArgumentException("등록되지 않은 스케줄입니다."));
        Comment comment = new Comment(requestDto);
        comment.setSchedule(schedule);
        Comment saveComment = commentRepository.save(comment);
        CommentResponseDto responseDto = new CommentResponseDto(saveComment);
        return responseDto;
    }

    public List<CommentResponseDto> findCommentById() {
        return commentRepository.findAll().stream().map(CommentResponseDto::new).toList();
    }

    @Transactional
    public CommentResponseDto updateComment(Long scheduleId, Long commentId, CommentRequestDto requestDto) {
        Comment comment = findCommentById(commentId);
        if (comment.getSchedule().getId() != scheduleId) {
            throw new IllegalArgumentException("수정할 수 없습니다");
        } else {
            comment.update(requestDto);
            comment = findCommentById(commentId);
            return new CommentResponseDto(comment);
        }
    }

    public void deleteComment(Long scheduleId, Long commentId) {
        Comment comment = findCommentById(commentId);
        // || comment.getUserId() != requestDto.getUserId()
        if (scheduleId != comment.getSchedule().getId()) {
            throw new IllegalArgumentException("삭제할 수 없는 댓글 입니다");
        } else {
            commentRepository.delete(comment);
        }
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("등록되지 않은 댓글입니다."));
    }

}
