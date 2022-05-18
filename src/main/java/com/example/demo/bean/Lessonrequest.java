package com.example.demo.bean;

import com.example.demo.bean.jsonUtils.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lessonrequest")
@Entity
public class Lessonrequest {//admin|student_self changeable

    @Id
    @JsonSerialize(using = IntegerToStringSerializer.class)
    @Column(name = "lessonrequestid", nullable = false, updatable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int lessonrequestid;//unchangeable

    @JsonSerialize(using = LessonSerializer.class)
    @JsonDeserialize(using = LessonDeserializer.class)
    @ManyToOne
    @JoinColumn(name = "lesson", updatable = false)
    private Lesson lesson;//unchangeable

    @JsonDeserialize(using = UserDeserializer.class)
    @JsonSerialize(using = UserSerializer.class)
    @ManyToOne
    @JoinColumn(name = "student", updatable = false)
    private User student;//unchangeable

    @Pattern(regexp = "20\\d{2}[ABCD]", message = "学期格式错误！")
    @Column(name = "semester", nullable = false, length = 10, updatable = false)
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
