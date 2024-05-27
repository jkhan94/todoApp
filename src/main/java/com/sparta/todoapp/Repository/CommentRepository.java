package com.sparta.todoapp.Repository;

import com.sparta.todoapp.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
