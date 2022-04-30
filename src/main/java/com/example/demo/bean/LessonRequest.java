package com.example.demo.bean;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lessonrequest")
@Entity
public class LessonRequest {//admin|student_self changeable

    @Id
    @Column(name = "lessonrequestid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lessonrequestid;//unchangeable

    @ManyToOne
    @JoinColumn(name = "lesson")
    private Lesson lesson;//unchangeable

    @ManyToOne
    @JoinColumn(name = "student")
    private User student;//unchangeable

    @Column(name = "semester", nullable = false, length = 10)
    private String semester;//unchangeable

    @Column(name = "request_reason", nullable = false, length = 1024)
    private String requestReason;//student_self changeable

    @Column(name = "status", nullable = false, length = 10)
    private Status status = Status.pending;//admin changeable

    public enum Status{
        pending, accepted, refused
    }

}
