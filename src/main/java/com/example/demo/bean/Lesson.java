package com.example.demo.bean;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @Column(name = "lessonid", nullable = false, length = 20)
    private String lessonid;

    @Column(name = "lessoncode", nullable = false, length = 20)
    private String lessoncode;

    @Column(name = "lessonname", nullable = false, length = 32)
    private String lessonname;

    @ManyToOne
    @JoinColumn(name = "school")
    private School school;

    @Column(name = "hour", nullable = false)
    private int hour;

    @Column(name = "credit", nullable = false)
    private int credit;

    @ManyToMany
    @JoinTable(name="lessons_teacher_taking")
    private List<User> teacher;

    @Column(name = "introduction")
    private String introduction;

    @OneToMany(mappedBy = "uplesson")
    private List<Classarrange> arranges;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "semester", length = 10)
    private String semester;

    @Column(name = "majorallowed")
    private String majorallowed;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.censored;

    public enum Status{
        censored, pending
    }

    @ManyToMany()
    @JoinTable(name="lessons_students_taking")
    private List<User> classmates;


}
