package com.example.demo.bean;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lessonrequest")
@Entity
public class LessonRequest {

    @Id
    @Column(name = "request_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestId;

    @ManyToOne
    @JoinColumn(name = "lesson")
    private Lesson lesson;

    @ManyToOne
    @JoinColumn(name = "student")
    private User student;

    @Column(name = "request_reason", nullable = false, length = 1024)
    private String requestReason;

    @Column(name = "status", nullable = false, length = 10)
    private Status status = Status.pending;

    public enum Status{
        pending, accepted, refused
    }

}
