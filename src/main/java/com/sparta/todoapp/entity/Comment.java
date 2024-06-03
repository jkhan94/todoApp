package com.sparta.todoapp.entity;

import com.sparta.todoapp.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor // JPA가 세팅할 때 기본 생성자가 필요. JPA 인스턴스화 리플렉션.
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "contents", nullable = false, length = 500)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Comment(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }

    public void update(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
