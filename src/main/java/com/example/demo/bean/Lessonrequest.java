package com.example.demo.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lessonrequest")
@Entity
public class Lessonrequest {//admin|student_self changeable

    @Id
    @Column(name = "lessonrequestid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lessonrequestid;//unchangeable

    @JsonIgnoreProperties(value = {
            "school", "hour", "teacher",
            "introduction", "arranges", "capacity", "semester",
            "majorallowed", "status"})
    @ManyToOne
    @JoinColumn(name = "lesson")
    private Lesson lesson;//unchangeable

    @JsonIgnoreProperties(value = {"usertype",
            "identitynumber","email","phonenumber", "status"})
    @ManyToOne
    @JoinColumn(name = "student")
    private User student;//unchangeable

    @Column(name = "semester", nullable = false, length = 10)
    private String semester;//unchangeable

    @Column(name = "request_reason", nullable = false, length = 1024)
    private String requestReason;//student_self changeable

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private Status status = Status.pending;//admin changeable

    public enum Status{
        pending, accepted, refused
    }

}
