package com.example.demo.bean;

import com.example.demo.bean.jsonUtils.LessonDeserializer;
import com.example.demo.bean.jsonUtils.LessonSerializer;
import com.example.demo.bean.jsonUtils.UserDeserializer;
import com.example.demo.bean.jsonUtils.UserSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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

    @JsonSerialize(using = LessonSerializer.class)
    @JsonDeserialize(using = LessonDeserializer.class)
    @ManyToOne
    @JoinColumn(name = "lesson")
    private Lesson lesson;//unchangeable

    @JsonDeserialize(using = UserDeserializer.class)
    @JsonSerialize(using = UserSerializer.class)
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
