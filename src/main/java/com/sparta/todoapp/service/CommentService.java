package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CommentRepository;
import com.sparta.todoapp.repository.ScheduleRepository;
import com.sparta.todoapp.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public CommentResponseDto createComment(Long scheduleId, CommentRequestDto requestDto, HttpServletRequest req) {
        Comment comment = new Comment(requestDto);

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new IllegalArgumentException("등록되지 않은 스케줄입니다."));
        comment.setSchedule(schedule);
        User user = (User) req.getAttribute("user");
        comment.setUser(user);

        Comment saveComment = commentRepository.save(comment);
        CommentResponseDto responseDto = new CommentResponseDto(saveComment);
        return responseDto;
    }

    // 댓글 목록 조회
    public List<CommentResponseDto> findCommentById() {
        return commentRepository.findAll().stream().map(CommentResponseDto::new).toList();
    }

    @Transactional
    public CommentResponseDto updateComment(Long scheduleId, Long commentId, CommentRequestDto requestDto, HttpServletRequest req) {
        Comment comment = findCommentById(commentId);
        User user = (User) req.getAttribute("user");
        if (comment.getSchedule().getId() != scheduleId || comment.getUser().getId() != user.getId()) {
            throw new IllegalArgumentException("수정할 수 없는 댓글입니다");
        } else {
            comment.update(requestDto);
            comment = findCommentById(commentId);
            return new CommentResponseDto(comment);
        }
    }

    public void deleteComment(Long scheduleId, Long commentId, HttpServletRequest req) {
        Comment comment = findCommentById(commentId);
        User user = (User) req.getAttribute("user");
        if (scheduleId != comment.getSchedule().getId() || comment.getUser().getId() != user.getId()) {
            throw new IllegalArgumentException("삭제할 수 없는 댓글 입니다");
        } else {
            commentRepository.delete(comment);
        }
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("등록되지 않은 댓글입니다."));
    }

}
