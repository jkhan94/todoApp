package com.sparta.todoapp.entity;

import com.sparta.todoapp.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "contents", nullable = false, length = 500)
    private String contents;
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    public Comment(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
        this.userId = requestDto.getUserId();
    }

}
