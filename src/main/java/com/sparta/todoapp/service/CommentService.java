package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.exception.CustomException;
import com.sparta.todoapp.repository.CommentRepository;
import com.sparta.todoapp.repository.ScheduleRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sparta.todoapp.exception.ErrorEnum.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    public CommentResponseDto createComment(Long scheduleId, CommentRequestDto requestDto, HttpServletRequest req) {
        Comment comment = new Comment(requestDto);

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new CustomException(SCHEDULE_NOT_FOUND));
        comment.setSchedule(schedule);
        User user = (User) req.getAttribute("user");
        comment.setUser(user);

        try {
            Comment saveComment = commentRepository.save(comment);
            CommentResponseDto responseDto = new CommentResponseDto(saveComment);
            return responseDto;
        } catch (Exception e) {
            throw new CustomException(NOT_SAVED);
        }

    }

    // 댓글 목록 조회
    public List<CommentResponseDto> findCommentById() {
        return commentRepository.findAll().stream().map(CommentResponseDto::new).toList();
    }

    @Transactional
    public CommentResponseDto updateComment(Long scheduleId, Long commentId, CommentRequestDto requestDto, HttpServletRequest req) {
        Comment comment = findCommentById(commentId);
        User user = (User) req.getAttribute("user");

        checkId(scheduleId, comment, user);

        try {
            comment.update(requestDto);
            comment = findCommentById(commentId);
            return new CommentResponseDto(comment);
        } catch (Exception e) {
            throw new CustomException(NOT_SAVED);
        }
    }

    public void deleteComment(Long scheduleId, Long commentId, HttpServletRequest req) {
        Comment comment = findCommentById(commentId);
        User user = (User) req.getAttribute("user");

        checkId(scheduleId, comment, user);

        try {
            commentRepository.delete(comment);
        } catch (Exception e) {
            throw new CustomException(NOT_SAVED);
        }
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
    }

    private static void checkId(Long scheduleId, Comment comment, User user) {
        if (!comment.getSchedule().getId().equals(scheduleId)) {
            throw new CustomException(SCHEDULE_NOT_FOUND);
        }
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(NOT_AVAILABLE_USER);
        }
    }

}
